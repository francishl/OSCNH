package com.huliang.oschn.improve.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huliang.oschn.R;

/**
 * Created by huliang on 17/3/18.
 */
public class TitleBar extends FrameLayout {

    private TextView mTitle;
    private ImageView mIcon;

    public TitleBar(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.lay_title_bar, this, true);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mIcon = (ImageView) findViewById(R.id.iv_icon);

        if (attrs != null) {
            // Load attributes 获取自定义属性的值
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar,
                    defStyleAttr, defStyleRes);

            String title = typedArray.getString(R.styleable.TitleBar_aTitle);
            Drawable icon = typedArray.getDrawable(R.styleable.TitleBar_aIcon);
            typedArray.recycle(); // 回收并复用

            mTitle.setText(title);
            mIcon.setImageDrawable(icon);
        } else {
            mIcon.setVisibility(GONE);
        }

        // Set Background
        setBackgroundColor(getResources().getColor(R.color.main_green));
    }

    public void setTitle(@StringRes int titleRes) {
        if (titleRes <= 0) {
            return;
        }
        mTitle.setText(titleRes);
    }

    public void setIcon(@DrawableRes int iconRes) {
        if (iconRes <= 0) {
            mIcon.setVisibility(GONE);
            return;
        }

        mIcon.setImageResource(iconRes);
        mIcon.setVisibility(VISIBLE);
    }

    /**
     * icon事件监听
     *
     * @param listener
     */
    public void setIconOnClickListener(OnClickListener listener) {
        mIcon.setOnClickListener(listener);
    }
}
