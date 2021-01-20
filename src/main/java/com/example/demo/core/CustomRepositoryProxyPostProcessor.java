package com.example.demo.core;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public class CustomRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {
    @Override
    public void postProcess(ProxyFactory proxyFactory, RepositoryInformation repositoryInformation) {
        proxyFactory.addAdvice(new JpqlResultMethodInterceptor(repositoryInformation));
    }
}
