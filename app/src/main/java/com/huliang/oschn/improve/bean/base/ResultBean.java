package com.huliang.oschn.improve.bean.base;

import com.huliang.oschn.improve.notice.NoticeBean;

/**
 * Created by huliang on 17/3/20.
 */
public class ResultBean<T> {
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_UNKNOW = 0;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_NOT_FIND = 404;
    public static final int RESULT_NOT_LOGIN = 201;
    public static final int RESULT_TOKEN_EXPRIED = 202;
    public static final int RESULT_NOT_PERMISSION = 203;
    public static final int RESULT_TOKEN_ERROR = 204;

    private T result;
    private int code;
    private String message;
    private String time;
    private NoticeBean notice;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }

    public boolean isOk() {
        return code == RESULT_SUCCESS;
    }

    public boolean isSuccess() {
        return code == RESULT_SUCCESS && result != null;
    }
}
