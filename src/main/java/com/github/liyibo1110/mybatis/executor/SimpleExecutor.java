package com.github.liyibo1110.mybatis.executor;

public class SimpleExecutor implements Executor {

    public <T> T query(String statement, Object[] parameter, Class type) {

        StatementHandler sh = new StatementHandler();
        return sh.query(statement, parameter, type);
    }
}
