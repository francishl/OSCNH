package com.huliang.oschn.improve.behavior;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huliang.oschn.R;

/**
 * 详情页输入框
 * <p/>
 * Created by huliang on 4/5/17.
 */
public class CommentBar {
    private View mRootView;
    private ViewGroup mParent;

    public static CommentBar delegation(Context context, ViewGroup parent) {
        CommentBar bar = new CommentBar();
        bar.mRootView = LayoutInflater.from(context).inflate(R.layout.layout_comment_bar, parent, false);
        bar.mParent = parent;
        bar.mParent.addView(bar.mRootView); // 添加 layout_comment_bar 视图到 parent 上
        return bar;
    }
}
