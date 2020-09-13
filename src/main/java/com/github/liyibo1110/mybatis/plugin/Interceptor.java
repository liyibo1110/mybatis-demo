package com.github.liyibo1110.mybatis.plugin;

public interface Interceptor {

    /**
     * 插件具体逻辑的实现方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 实现拦截的对象如何生成代理
     * @param object
     * @return
     */
    Object plugin(Object object);
}
