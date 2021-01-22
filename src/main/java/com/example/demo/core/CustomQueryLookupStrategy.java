package com.example.demo.core;

import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CustomQueryLookupStrategy implements QueryLookupStrategy {
    private final EntityManager em;
    private QueryLookupStrategy queryLookupStrategy;
    private final QueryExtractor extractor;
    private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;

    public CustomQueryLookupStrategy(EntityManager em,
//                                     JpaQueryMethodFactory factory,
                                     Key key, QueryExtractor extractor, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        Assert.notNull(em, "EntityManager must not be null!");
        this.em = em;
        this.extractor = extractor;
        JpaQueryMethodFactory factory = new DefaultJpaQueryMethodFactory(extractor);
        this.queryLookupStrategy = JpaQueryLookupStrategy.create(em, factory, key, evaluationContextProvider,
                escapeCharacter);
    }

    public static QueryLookupStrategy create(EntityManager em, @Nullable Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        QueryExtractor extractor = PersistenceProvider.fromEntityManager(em);

        return new CustomQueryLookupStrategy(em, key, extractor, evaluationContextProvider);
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata repositoryMetadata, ProjectionFactory projectionFactory, NamedQueries namedQueries) {
        Query query = method.getAnnotation(Query.class);
        if (query != null) {
            if (isConvertable(method, repositoryMetadata)) {
                DefaultConversionService sharedInstance = (DefaultConversionService) DefaultConversionService.getSharedInstance();
                sharedInstance.addConverter(new MapToEntityConverter());
            }
        }
        return queryLookupStrategy.resolveQuery(method, repositoryMetadata, projectionFactory, namedQueries);
    }

    private boolean isConvertable(Method method, RepositoryMetadata repositoryMetadata) {
        Class<?> returnType = method.getReturnType();
        Class<?> domainType = repositoryMetadata.getDomainType();
        if (Iterable.class.isAssignableFrom(returnType)) {
            Type grt = method.getGenericReturnType();
            if (grt instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) grt;
                Class<?> argument = (Class<?>) pt.getActualTypeArguments()[0];
                if (!domainType.isAssignableFrom(argument)) {
                    boolean assignableFrom = Convertable.class.isAssignableFrom(argument);
                    Assert.state(assignableFrom, String.format("method returnType's argument must implements Convertable interface! %s %s.%s",
                            method.getGenericReturnType(), method.getDeclaringClass().getSimpleName(), method.getName()));
                    return true;
                }
            }
        } else {
            boolean isExtendDomainType = domainType.isAssignableFrom(returnType);
            boolean is = Convertable.class.isAssignableFrom(returnType);
            if (!isExtendDomainType) {
                Assert.state(is, String.format("method returnType must implements Convertable interface! %s %s.%s",
                        method.getGenericReturnType(), method.getDeclaringClass().getSimpleName(), method.getName()));
                return true;
            }
        }
        return false;
    }
}
