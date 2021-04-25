package com.example.demo.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeignAspect {
    private static final Logger log = LogManager.getLogger(FeignAspect.class);

    @Around("execution(public * com.example.demo.feign.*.*(..))")
    public Object around(ProceedingJoinPoint pj) throws Throwable{
        log.info(">>>>>>>>> {}",pj.getArgs());
        return pj.proceed();
    }
}
