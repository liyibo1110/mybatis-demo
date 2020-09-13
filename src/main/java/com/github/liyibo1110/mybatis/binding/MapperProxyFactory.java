package com.github.liyibo1110.mybatis.binding;

import com.github.liyibo1110.mybatis.session.DefaultSqlSession;

import java.lang.reflect.Proxy;

/**
 * Mapper接口的动态代理对象的工厂
 */
public class MapperProxyFactory<T> {

    private Class<T> mapperInterface;
    private Class resultType;

    public MapperProxyFactory(Class<T> mapperInterface, Class resultType) {
        this.mapperInterface = mapperInterface;
        this.resultType = resultType;
    }

    public T newInstance(DefaultSqlSession sqlSession) {
        return (T)Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new MapperProxy(sqlSession, resultType));
    }
}
