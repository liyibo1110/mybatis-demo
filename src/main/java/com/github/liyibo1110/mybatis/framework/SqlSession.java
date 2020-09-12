package com.github.liyibo1110.mybatis.framework;

public class SqlSession {

    private Configuration configuration;
    private Executor executor;

    public <T> T selectOne(String statementId, Object parameter) {
        String sql = statementId;
        return executor.query(sql, parameter);
    }

    /**
     * 获取代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class clazz) {
        return configuration.getMapper(clazz);
    }
}
