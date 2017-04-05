package com.huliang.oschn.improve.tweet.contract;

import com.huliang.oschn.base.User;
import com.huliang.oschn.improve.bean.Tweet;
import com.huliang.oschn.improve.bean.simple.TweetComment;

/**
 * Created by huliang on 3/29/17.
 */
public interface TweetDetailContract {

    /**
     * 详情页全局操作回调: 网络加载, 评论框等
     */
    interface Operator {
        Tweet getTweetDetail();

        void toReply(TweetComment comment);

        void onScroll();
    }

    /**
     * 评论操作回调
     */
    interface ICmnView {
        void onCommentSuccess(TweetComment comment);
    }

    /**
     * 点赞操作回调
     */
    interface IThumbupView {
        void onLikeSuccess(boolean isUp, User user);
    }

    /**
     * "赞+评论" tabLayout 区域回调
     */
    interface IAgencyView {
        void resetLikeCount(int count);

        void resetCmnCount(int count);
    }
}
