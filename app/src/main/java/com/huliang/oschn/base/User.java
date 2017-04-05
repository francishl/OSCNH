package com.huliang.oschn.base;

import com.huliang.oschn.bean.Entity;

/**
 * 登录用户实体类
 * <p>
 * Created by huliang on 3/31/17.
 */
public class User extends Entity {
    public final static int RELATION_ACTION_DELETE = 0x00;// 取消关注
    public final static int RELATION_ACTION_ADD = 0x01;// 加关注

    @Deprecated
    public final static int RELATION_TYPE_BOTH = 0x01;// 双方互为粉丝
    @Deprecated
    public final static int RELATION_TYPE_FANS_HIM = 0x02;// 你单方面关注他
    @Deprecated
    public final static int RELATION_TYPE_NULL = 0x03;// 互不关注
    @Deprecated
    public final static int RELATION_TYPE_FANS_ME = 0x04;// 只有他关注我

    public final static int RELATION_TYPE_APIV2_BOTH = 0x01;// 双方互为粉丝
    public final static int RELATION_TYPE_APIV2_ONLY_FANS_HIM = 0x02;// 你单方面关注他
    public final static int RELATION_TYPE_APIV2_ONLY_FANS_ME = 0x03;// 只有他关注我
    public final static int RELATION_TYPE_APIV2_NULL = 0x04;// 互不关注

    private int id;

    private String location;

    private String name;

    private int followers;

    private int fans;

    private int score;

    private String portrait;

    private String jointime;

    private String gender;

    private String devplatform;

    private String expertise;

    private int relation;

    private String latestonline;

    private String from;

    private int favoritecount;

    private String account;

    private String pwd;

    private boolean isRememberMe;
}
