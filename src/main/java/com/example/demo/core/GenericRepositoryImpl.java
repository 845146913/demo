package com.example.demo.core;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * author: wangshuiping
 * date: 2021/1/20
 */
public class GenericRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }
}
