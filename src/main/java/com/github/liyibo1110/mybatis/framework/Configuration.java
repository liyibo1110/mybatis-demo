package com.github.liyibo1110.mybatis.framework;

import java.lang.reflect.Proxy;

public class Configuration {

    /**
     * 返回接口的代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class clazz) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new MapperProxy());
    }
}
