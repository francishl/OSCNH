package com.huliang.oschn.improve.media;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by huliang on 3/23/17.
 */
public class ImagePreviewView extends ImageView {
    public ImagePreviewView(Context context) {
        super(context);
    }

    public ImagePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImagePreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
