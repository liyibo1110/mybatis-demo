package com.github.liyibo1110.mybatis.binding;

import com.github.liyibo1110.mybatis.session.DefaultSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper接口的动态代理实现
 */
public class MapperProxy implements InvocationHandler {

    private DefaultSqlSession sqlSession;
    private Class resultType;

    public MapperProxy(DefaultSqlSession sqlSession, Class resultType) {
        this.sqlSession = sqlSession;
        this.resultType = resultType;
    }

    /**
     * 调Mapper接口方法就会执行这个方法了
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String mapperClassName = method.getDeclaringClass().getName();
        String statementId = mapperClassName + "." + method.getName();
        if(sqlSession.getConfiguration().hasStatement(statementId)) {
            return sqlSession.selectOne(statementId, args, resultType);
        }else {
            return method.invoke(proxy, args);
        }
    }
}
