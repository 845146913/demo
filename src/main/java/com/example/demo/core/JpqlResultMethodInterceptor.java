package com.example.demo.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.core.RepositoryInformation;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public class JpqlResultMethodInterceptor implements MethodInterceptor {
    public JpqlResultMethodInterceptor(RepositoryInformation repositoryInformation) {
        Iterator<Method> iterable = repositoryInformation.getQueryMethods().iterator();
//        SqlParser sqlParser = new DefaultSqlParser();
        while (iterable.hasNext()) {
            Method method = iterable.next();
            Query query = method.getAnnotation(Query.class);
            if (query == null || query.nativeQuery()) {
                continue;
            }
            //获取返回类型
//            Class clazz = getGenericReturnClass(method);
//            if (clazz == null) {
//                continue;
//            }
//            SelectAlias alias = sqlParser.getAlias(query.value(), clazz);
//            if (alias == null) {
//                continue;
//            }
//            selectAlias.put(method, alias)
            Class<?> clazz = method.getReturnType();
            if(Objects.isNull(clazz)) {
                continue;
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        NativeQuery annotation = AnnotationUtils.findAnnotation(method, NativeQuery.class);
        if(annotation != null) {
            return null;
        } else {
            Object obj = methodInvocation.proceed();
            return obj;
        }
    }
}
