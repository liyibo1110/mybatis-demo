package com.github.liyibo1110.mybatis.cache;

/**
 * 算法是抄的
 */
public class CacheKey {

    private static final int DEFAULT_HASHCODE = 17;
    private static final int DEFAULT_MULTIPLIER = 37;

    private int hashCode;
    private int multiplier;
    private int count;

    public CacheKey() {
        this.hashCode = DEFAULT_HASHCODE;
        this.multiplier = DEFAULT_MULTIPLIER;
        this.count = 0;
    }

    public int getCode() {
        return hashCode;
    }

    public void update(Object object) {
        int base = object == null ? 1 : object.hashCode();
        count++;
        base *= count;
        hashCode = multiplier * hashCode + base;
    }
}
