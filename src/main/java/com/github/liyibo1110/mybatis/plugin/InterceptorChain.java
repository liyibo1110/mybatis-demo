package com.github.liyibo1110.mybatis.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装所有插件拦截器的责任链类
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {

        interceptors.add(interceptor);
    }

    /**
     * 将一个对象经过所有插件拦截器代理，生成最终的代理对象
     * @param target
     * @return
     */
    public Object pluginAll(Object target) {

        for(Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public boolean hasPlugin() {

        if(interceptors.size() > 0) {
            return true;
        }else {
            return false;
        }
    }
}
