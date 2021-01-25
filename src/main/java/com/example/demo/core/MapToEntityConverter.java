package com.example.demo.core;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/22
 */
public class MapToEntityConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair pair = new ConvertiblePair(Map.class, Object.class);
        return Collections.singleton(pair);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            Map<String, Object> sourceMap = (Map<String, Object>) source;
            Class<?> clazz = Class.forName(targetType.getName());
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            // 设置私有构造访问权限
            constructor.setAccessible(true);
            Object target = constructor.newInstance();
            sourceMap.entrySet().forEach(entry -> {
                String key = entry.getKey();
                String methodName = getMethodName(key);
                Method[] methods = ReflectionUtils.getDeclaredMethods(targetType.getType());
                Method method = Stream.of(methods).filter(me -> me.getName().equalsIgnoreCase(methodName))
                        .findFirst().orElse(null);
                try {
                    Objects.requireNonNull(method).invoke(target, caseValue(entry.getValue()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            return target;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private String getMethodName(String field) {
        String[] split = field.split("_");
        if (split.length > 0) {
            String property = Stream.of(split).reduce((a, b) -> translateUpperCamelCase(a) + translateUpperCamelCase(b)).get();
            return "set" + property;
        }
        return "set" + translateUpperCamelCase(field);
    }

    public Object caseValue(Object value) {
        if (value instanceof java.math.BigInteger) {
            return ((BigInteger) value).longValue();
        }
        return value;
    }

    public String translateUpperCamelCase(String input) {
        if (input == null || input.length() == 0) {
            return input; // garbage in, garbage out
        }
        // Replace first lower-case letter with upper-case equivalent
        char c = input.charAt(0);
        char uc = Character.toUpperCase(c);
        if (c == uc) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        sb.setCharAt(0, uc);
        return sb.toString();
    }

    public String translateSnakeCase(String input) {
        if (input == null) return input; // garbage in, garbage out
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (i > 0 || c != '_') // skip first starting underscore
            {
                if (Character.isUpperCase(c)) {
                    if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                        result.append('_');
                        resultLength++;
                    }
                    c = Character.toLowerCase(c);
                    wasPrevTranslated = true;
                } else {
                    wasPrevTranslated = false;
                }
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }
}
