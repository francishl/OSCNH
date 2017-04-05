package com.huliang.oschn.improve.tweet.fragments;

import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.base.fragments.BaseRecyclerViewFragment;
import com.huliang.oschn.improve.bean.simple.TweetComment;
import com.huliang.oschn.improve.tweet.adapter.TweetCommentAdapter;

import java.lang.reflect.Type;

/**
 * 动弹详情, 评论列表
 * <p>
 * Created by huliang on 3/28/17.
 */
public class ListTweetCommentFragment extends BaseRecyclerViewFragment<TweetComment> {
    @Override
    protected BaseRecyclerAdapter<TweetComment> getRecyclerAdapter() {
        return new TweetCommentAdapter(getContext());
    }

    @Override
    protected Type getType() {
        return null;
    }
}
