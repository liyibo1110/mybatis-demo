package com.github.liyibo1110.mybatis.executor;

public interface Executor {

    <T> T query(String statement, Object[] parameter, Class type);
}
