package com.huliang.oschn.improve.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.adapter.BaseGeneralRecyclerAdapter;
import com.huliang.oschn.improve.bean.Tweet;
import com.huliang.oschn.improve.bean.simple.Author;
import com.huliang.oschn.improve.widget.TweetPicturesLayout;
import com.huliang.oschn.util.TLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 基本列表类 BaseRecyclerViewFragment, 动弹页面 adapter
 * <p/>
 * Created by huliang on 17/3/20.
 */
public class UserTweetAdapter extends BaseGeneralRecyclerAdapter<Tweet> implements View.OnClickListener {
    private static final String TAG = "UserTweetAdapter";

    public UserTweetAdapter(Context context) {
        super(context, ONLY_FOOTER);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_tweet_improve,
                parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, Tweet item, int position) {
        ViewHolder holder = (ViewHolder) h;

        // author
        final Author author = item.getAuthor();
        if (author == null) {
            holder.mViewPortrait.setImageResource(R.mipmap.widget_default_face);
            holder.mViewName.setText("匿名用户");
        } else {
            // 加载头像
            Glide.with(mContext)
                    .load(author.getPortrait())
                    .asBitmap()
                    .placeholder(R.mipmap.widget_default_face)
                    .error(R.mipmap.widget_default_face)
                    .into(holder.mViewPortrait);
            // 点击头像
            holder.mViewPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppContext.context(), "点击头像", Toast.LENGTH_SHORT).show();
                }
            });
            holder.mViewName.setText(author.getName());
        }

        // content
        if (!TextUtils.isEmpty(item.getContent())) {
            holder.mViewContent.setText(item.getContent());
        }

        // timestamp + platform
        holder.mViewTime.setText(item.getPubDate());
        holder.mViewPlatform.setText(String.valueOf(item.getAppClient()));

        // images
        Tweet.Image[] images = item.getImages();
        holder.mLayoutFlow.setImages(images);

        // statistics
        if (item.getStatistics() != null) {
            holder.mViewCmmCount.setText(String.valueOf(item.getStatistics().getComment()));
            holder.mViewLikeCount.setText(String.valueOf(item.getStatistics().getLike()));
            int mDispatchCount = item.getStatistics().getTransmit();
            if (mDispatchCount <= 0) {
                holder.mViewDispatchCount.setText("转发");
            } else {
                holder.mViewDispatchCount.setVisibility(View.VISIBLE);
                holder.mViewLikeCount.setText(String.valueOf(item.getStatistics().getTransmit()));
            }
        } else {
            holder.mViewCmmCount.setText(String.valueOf(item.getCommentCount()));
            holder.mViewLikeCount.setText(String.valueOf(item.getLikeCount()));
            holder.mViewDispatchCount.setVisibility(View.GONE);
        }

        String txtLikeCount = holder.mViewLikeCount.getText().toString();
        holder.mViewLikeCount.setText("0".equals(txtLikeCount) ? "赞" : txtLikeCount);

        String txtCommCount = holder.mViewCmmCount.getText().toString();
        holder.mViewCmmCount.setText("0".equals(txtCommCount) ? "评论" : txtCommCount);

    }

    /**
     * 点击 about 区域时触发
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        TLog.log("" + v.getTag());
    }

    /**
     * 自定义: Tweet Item View Holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_tweet_face)
        CircleImageView mViewPortrait;
        @Bind(R.id.tv_tweet_name)
        TextView mViewName;
        @Bind(R.id.tweet_item)
        TextView mViewContent;
        @Bind(R.id.fl_image)
        TweetPicturesLayout mLayoutFlow;
        @Bind(R.id.tv_tweet_time)
        TextView mViewTime;
        @Bind(R.id.tv_tweet_platform)
        TextView mViewPlatform;
        @Bind(R.id.tv_tweet_like_count)
        TextView mViewLikeCount;
        @Bind(R.id.tv_tweet_comment_count)
        TextView mViewCmmCount;
        @Bind(R.id.tv_dispatch_count)
        TextView mViewDispatchCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
