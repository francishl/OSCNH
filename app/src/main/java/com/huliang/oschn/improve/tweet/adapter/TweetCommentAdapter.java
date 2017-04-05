package com.huliang.oschn.improve.tweet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.bean.simple.TweetComment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huliang on 3/28/17.
 */
public class TweetCommentAdapter extends BaseRecyclerAdapter<TweetComment> {

    public TweetCommentAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new TweetCommentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.list_item_tweet_comment, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, TweetComment item, int position) {
        TweetCommentViewHolder holder = (TweetCommentViewHolder) h;
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
        holder.tvTime.setText(item.getPubDate());
        holder.tvContent.setText(item.getContent());
    }

    public static final class TweetCommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_avatar)
        CircleImageView ivPortrait;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_pub_date)
        TextView tvTime;
        @Bind(R.id.btn_comment)
        ImageView btnReply;
        @Bind(R.id.tv_content)
        TextView tvContent;

        public TweetCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
