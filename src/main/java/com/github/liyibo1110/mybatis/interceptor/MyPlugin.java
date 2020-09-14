package com.github.liyibo1110.mybatis.interceptor;

import com.github.liyibo1110.mybatis.annotation.Intercepts;
import com.github.liyibo1110.mybatis.plugin.Interceptor;
import com.github.liyibo1110.mybatis.plugin.Invocation;
import com.github.liyibo1110.mybatis.plugin.Plugin;

import java.util.Arrays;

/**
 * 自定义的插件
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String statement = (String)invocation.getArgs()[0];
        Object[] parameter = (Object[])invocation.getArgs()[1];
        Class resultType = (Class)invocation.getArgs()[2];
        System.out.println("插件输出SQL：[" + statement + "]");
        System.out.println("插件输出参数Parameter：" + Arrays.toString(parameter));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object object) {
        return Plugin.wrap(object, this);
    }
}
