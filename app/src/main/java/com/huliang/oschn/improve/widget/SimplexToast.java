package com.huliang.oschn.improve.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 将一个吐司单例化，并且作防止频繁点击的处理。
 * <p>
 * Created by huliang on 3/28/17.
 */
public class SimplexToast {
    private static Toast mToast;
    private static int yOffset; // y坐标

    public static Toast init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context should not be null!!!");
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            yOffset = mToast.getYOffset();
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, yOffset);
        mToast.setMargin(0, 0);
        return mToast;
    }

    public static void show(Context context, String content, int gravity, int duration) {
        if (mToast == null) {
            mToast = init(context.getApplicationContext());
        }
        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.setGravity(gravity, 0, yOffset);
        mToast.show();
    }
}
