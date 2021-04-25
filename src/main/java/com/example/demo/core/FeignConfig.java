package com.example.demo.core;

import com.example.demo.annotation.ResponseEntity;
import com.example.demo.feign.RealLevelRolesResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.Target;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.PageableSpringEncoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

@Configuration
public class FeignConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> httpMessageConverters;
    @Autowired(required = false)
    private SpringDataWebProperties springDataWebProperties;

    private static class CustomDecoder implements Decoder{
        final Decoder delegate;
        @Autowired
        private ObjectMapper om;

        private CustomDecoder(Decoder delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
            Method method = response.request().requestTemplate().methodMetadata().method();
            ResponseEntity ann = AnnotationUtils.findAnnotation(method, ResponseEntity.class);
            if (ann != null) {
                if (response.status() == 404 || response.status() == 204) {
                    return null;
                }
                String dc = response.body().toString();
                try {
                    JavaType javaType = TypeFactory.defaultInstance().constructType(type);
                    Object value = om.readValue(dc, javaType);
                    return value;
                } catch (JsonProcessingException e) {
                    return null;
                }
            } else {
                return delegate.decode(response, type);
            }
        }
    }
    private static class CustomEncoder implements Encoder {
        final Encoder delegate;

        private CustomEncoder(Encoder delegate) {
            this.delegate = delegate;
        }

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            Target<?> target = template.feignTarget();
            System.out.println(target);
            String reqMethod = template.method();
            if("GET".equals(reqMethod)) {
                Method method = template.methodMetadata().method();
                HandlerMethod hm = new HandlerMethod(object, method);
                MethodParameter[] methodParameters = hm.getMethodParameters();
                boolean anyMatch = Arrays.asList(methodParameters)
                        .stream()
                        .anyMatch(methodParameter ->
                    methodParameter.hasParameterAnnotation(RequestBody.class) && bodyType.equals(methodParameter.getParameterType())
                );
                if(anyMatch) {

                    templateWrapper(object, template);
                    return;
                }
                delegate.encode(object, bodyType, template);
                return;
//                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//                if (parameterAnnotations != null) {
//                    boolean anyMatch = isAnyMatch(parameterAnnotations, RequestBody.class);
//                    if(anyMatch) {
//                        templateWrapper(object, template);
//                    }
//                }
            }
            delegate.encode(object, bodyType, template);
        }

        private void templateWrapper(Object object, RequestTemplate template) {
            if (object instanceof Map) {
                ((Map)object).forEach((k,v) -> {
                    if(Objects.nonNull(v)) {
                        template.query(k.toString(), v.toString());
                    }
                });
                return;
            }
            Map<String, Object> map =new HashMap<>();
            Class<?> aClass = object.getClass();
            JsonNaming jsonNaming = AnnotationUtils.findAnnotation(aClass, JsonNaming.class);
            PropertyNamingStrategy.SnakeCaseStrategy snakeCaseStrategy = null;
            if(jsonNaming != null) {
                Class<? extends PropertyNamingStrategy> value = jsonNaming.value();
                if(value.isAssignableFrom(PropertyNamingStrategy.SnakeCaseStrategy.class)) {
                    snakeCaseStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();
                }
            }
            PropertyNamingStrategy.SnakeCaseStrategy finalSnakeCaseStrategy = snakeCaseStrategy;
            ReflectionUtils.doWithFields(aClass, field -> {
                String name = field.getName();
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, object);
                if (Objects.nonNull(value))
                    map.put(finalSnakeCaseStrategy == null?name: finalSnakeCaseStrategy.translate(name), value);
            });
            map.forEach((k,v)->{
                template.query(k, v.toString());
            });
        }
    }
    @Bean
    Decoder decoder(){
        return new CustomDecoder(new OptionalDecoder(
                new ResponseEntityDecoder(new SpringDecoder(this.httpMessageConverters))));
    }
    @Bean
    @ConditionalOnMissingClass("org.springframework.data.domain.Pageable")
    Encoder encoder() {
        return new CustomEncoder(new SpringEncoder(this.httpMessageConverters));
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.data.domain.Pageable")
    public Encoder feignEncoderPageable() {
        PageableSpringEncoder encoder = new PageableSpringEncoder(
                new SpringEncoder(this.httpMessageConverters));
        if (springDataWebProperties != null) {
            encoder.setPageParameter(
                    springDataWebProperties.getPageable().getPageParameter());
            encoder.setSizeParameter(
                    springDataWebProperties.getPageable().getSizeParameter());
            encoder.setSortParameter(
                    springDataWebProperties.getSort().getSortParameter());
        }
        return new CustomEncoder(encoder);
    }

    private static boolean isAnyMatch(Annotation[][] parameterAnnotations, Class<?> annotationClass) {
        boolean anyMatch = false;
        int length = parameterAnnotations.length;
        for (int i=0; i<length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            anyMatch = Arrays.asList(parameterAnnotation).stream().anyMatch(p -> Objects.equals(p.annotationType(), annotationClass));
            if(anyMatch) {
                break;
            }
        }
        return anyMatch;
    }


    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"ret\":301,\"msg\":\"\\u5fc5\\u8981\\u7684\\u53c2\\u6570\\u4e3a\\u7a7a\",\"data\":[]}";
        ObjectMapper om = new ObjectMapper();
        RealLevelRolesResp x = om.readValue(json, RealLevelRolesResp.class);

        System.out.println(x);
    }
}
