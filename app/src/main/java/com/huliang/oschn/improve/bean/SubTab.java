package com.huliang.oschn.improve.bean;

import java.io.Serializable;

/**
 * Created by huliang on 4/5/17.
 */
public class SubTab implements Serializable {

    private String token;
    private String name;
    private boolean fixed;
    private boolean needLogin;
    private String tag;
    private int type;
    private int subtype;
    private int order;
    private String href;
    private Banner banner;
    private boolean isActived;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }

    public class Banner implements Serializable {
        private int catalog;
        private String href;

        public int getCatalog() {
            return catalog;
        }

        public void setCatalog(int catalog) {
            this.catalog = catalog;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
