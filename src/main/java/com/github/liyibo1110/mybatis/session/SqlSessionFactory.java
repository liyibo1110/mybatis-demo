package com.github.liyibo1110.mybatis.session;

import com.github.liyibo1110.mybatis.framework.SqlSession;

public class SqlSessionFactory {

    private Configuration configuration;

    public SqlSessionFactory build() {
        configuration = new Configuration();
        return this;
    }

    public DefaultSqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
