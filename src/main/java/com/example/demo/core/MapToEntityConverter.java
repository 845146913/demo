package com.example.demo.core;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
        System.out.println("source:" + source);
        System.out.println("sourceType:" + sourceType);
        System.out.println("targetType:" + targetType);
        try {
            Class<?> clazz = Class.forName(targetType.getName());
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            // 设置私有构造访问权限
            constructor.setAccessible(true);
            Object target = constructor.newInstance();
            BeanUtils.copyProperties(source, target);
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
}
