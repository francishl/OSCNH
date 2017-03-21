package com.huliang.oschn.improve.notice;

import java.io.Serializable;

/**
 * Created by huliang on 17/3/20.
 */
public class NoticeBean implements Serializable {
    private int mention;
    private int letter;
    private int review;
    private int fans;
    private int like = 0;

    public int getMention() {
        return mention;
    }

    public void setMention(int mention) {
        this.mention = mention;
    }

    public int getLetter() {
        return letter;
    }

    public void setLetter(int letter) {
        this.letter = letter;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getLike() {
        return like;
    }

    /**
     * this.like = 0
     *
     * @param like
     */
    public void setLike(int like) {
        this.like = like;
    }
}
