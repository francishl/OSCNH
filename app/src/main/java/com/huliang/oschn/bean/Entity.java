package com.huliang.oschn.bean;

/**
 * 实体类
 * <p>
 * Created by huliang on 3/31/17.
 */
public abstract class Entity extends Base {
    protected int id;

    protected String cacheKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
