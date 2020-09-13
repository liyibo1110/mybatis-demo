package com.github.liyibo1110.mybatis.binding;

import com.github.liyibo1110.mybatis.session.DefaultSqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    /**
     * 保存接口Class对象和生成动态代理对象的工厂类映射
     */
    private final Map<Class<?>, MapperProxyFactory> knownMappers = new HashMap<>();

    public <T> void addMapper(Class<T> mapperType, Class resultType) {
        knownMappers.put(mapperType, new MapperProxyFactory(mapperType, resultType));
    }

    /**
     * 创建Mapper的代理对象
     * @param mapperType
     * @param sqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> mapperType, DefaultSqlSession sqlSession) {
        MapperProxyFactory factory = knownMappers.get(mapperType);
        if(factory == null) throw new RuntimeException("Type: " + mapperType + " can not be found");
        return (T)factory.newInstance(sqlSession);
    }
}
