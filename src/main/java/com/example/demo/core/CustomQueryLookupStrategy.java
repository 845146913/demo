package com.example.demo.core;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.Query;
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
import java.util.Set;

public class CustomQueryLookupStrategy implements QueryLookupStrategy {
    private final EntityManager em;
    private QueryLookupStrategy queryLookupStrategy;

    public CustomQueryLookupStrategy(EntityManager em,
                                     Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        Assert.notNull(em, "EntityManager must not be null!");
        this.em = em;
        this.queryLookupStrategy = CustomQueryLookupStrategy.create(em, key, evaluationContextProvider);
    }

    public static QueryLookupStrategy create(EntityManager em, @Nullable Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        return new CustomQueryLookupStrategy(em, key, evaluationContextProvider);
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata repositoryMetadata, ProjectionFactory projectionFactory, NamedQueries namedQueries) {
        Query query = method.getAnnotation(Query.class);
        if (query != null && query.nativeQuery()) {
            ((DefaultConversionService) DefaultConversionService.getSharedInstance())
                    .addConverter(new GenericConverter() {
                        @Override
                        public Set<ConvertiblePair> getConvertibleTypes() {
                            return null;
                        }

                        @Override
                        public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
                            return null;
                        }
                    });
        }
        return queryLookupStrategy.resolveQuery(method, repositoryMetadata, projectionFactory, namedQueries);
    }
}
