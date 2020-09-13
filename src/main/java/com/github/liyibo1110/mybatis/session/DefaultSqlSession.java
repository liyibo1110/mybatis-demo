package com.github.liyibo1110.mybatis.session;

import com.github.liyibo1110.mybatis.executor.Executor;

public class DefaultSqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = configuration.newExecutor();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }

    public <T> T selectOne(String statement, Object[] parameter, Class resultType) {
        String sql = getConfiguration().getMappedStatement(statement);
        return executor.query(sql, parameter, resultType);
    }
}
