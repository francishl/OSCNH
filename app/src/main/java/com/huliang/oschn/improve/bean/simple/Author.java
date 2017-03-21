package com.huliang.oschn.improve.bean.simple;

import java.io.Serializable;

/**
 * Created by huliang on 17/3/20.
 */
public class Author implements Serializable {
    protected long id;
    protected String name;
    protected String portrait;
    protected int relation;
    protected int gender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
