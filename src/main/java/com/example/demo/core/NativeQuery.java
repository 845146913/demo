package com.example.demo.core;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
@QueryAnnotation
@Documented
public @interface NativeQuery {
    String value();
    String countSql() default "";
}
