package com.huliang.oschn.improve.tweet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.bean.simple.TweetLike;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huliang on 3/28/17.
 */
public class TweetLikeUsersAdapter extends BaseRecyclerAdapter<TweetLike> {

    public TweetLikeUsersAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new LikeUsersViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.list_cell_tweet_like_user, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, TweetLike item, int position) {
        LikeUsersViewHolder holder = (LikeUsersViewHolder) h;
        if (TextUtils.isEmpty(item.getAuthor().getPortrait())) {
            holder.ivPortrait.setImageResource(R.mipmap.widget_default_face);
        } else {
            Glide.with(mContext)
                    .load(item.getAuthor().getPortrait())
                    .asBitmap()
                    .placeholder(R.mipmap.widget_default_face)
                    .error(R.mipmap.widget_default_face)
                    .into(holder.ivPortrait);
        }
        holder.tvName.setText(item.getAuthor().getName());
    }

    /**
     * 自定义: 点赞列表 View Holder
     */
    public static final class LikeUsersViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_avatar)
        CircleImageView ivPortrait;
        @Bind(R.id.tv_name)
        TextView tvName;

        public LikeUsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
