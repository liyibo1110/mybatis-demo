package com.github.liyibo1110.mybatis.executor;

import com.github.liyibo1110.mybatis.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;

public class CachingExecutor implements Executor {

    private Executor delegate;
    private static final Map<Integer, Object> cache = new HashMap<>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(String statement, Object[] parameter, Class type) {

        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(joinStr(parameter));
        if(cache.containsKey(cacheKey.getCode())) {
            // 命中
            return (T)cache.get(cacheKey.getCode());
        }else {
            // 未命中则调用原始Executor获取，并放入缓存
            Object obj = delegate.query(statement, parameter, type);
            cache.put(cacheKey.getCode(), obj);
            return (T)obj;
        }
    }

    /**
     * 对象值用逗号分隔
     * @param objs
     * @return
     */
    private String joinStr(Object[] objs) {

        StringBuilder sb = new StringBuilder();
        if(objs != null && objs.length > 0) {
            for(Object obj : objs) {
                sb.append(obj.toString()).append(",");
            }
        }
        if(sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
