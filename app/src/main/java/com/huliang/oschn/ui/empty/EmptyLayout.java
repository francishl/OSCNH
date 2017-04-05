package com.huliang.oschn.ui.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huliang.oschn.R;

import net.oschina.common.widget.Loading;

/**
 * Created by huliang on 3/28/17.
 */
public class EmptyLayout extends LinearLayout {
    private Loading mLoading;

    private final Context context;

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_error_layout, this, false);
        mLoading = (Loading) view.findViewById(R.id.animProgress);

        addView(view);
    }

    public void setErrorType(int i) {
        setVisibility(VISIBLE);
        switch (i) {
            case NETWORK_LOADING:
                mLoading.start();
                break;
            case HIDE_LAYOUT:
                mLoading.stop();
                setVisibility(GONE);
                break;
        }
    }
}
