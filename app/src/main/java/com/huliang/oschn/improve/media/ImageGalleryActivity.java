package com.huliang.oschn.improve.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.activities.BaseActivity;

import net.oschina.common.widget.Loading;

import butterknife.OnClick;

/**
 * 图片预览Activity, 使用 viewPager 来显示多图
 * <p/>
 * Created by huliang on 3/23/17.
 */
public class ImageGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String KEY_IMAGE = "images";
    public static final String KEY_COOKIE = "cookie_need";
    public static final String KEY_POSITION = "position";
    public static final String KEY_NEED_SAVE = "save";

    private PreviewerViewPager mImagePager;
    private TextView mIndexText;

    private String[] mImageSources;
    private int mCurPosition;
    private boolean mNeedSaveLocal;
    private boolean mNeedCookie;
    private boolean[] mImageDownloadStatus;

    @Override
    protected int getContentView() {
        return R.layout.activity_image_gallery;
    }

    public static void show(Context context, String[] images, int position) {
        show(context, images, position, true);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal) {
        show(context, images, position, needSaveLocal, false);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal,
                            boolean needCookie) {
        if (images == null || images.length == 0) {
            return;
        }
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra(KEY_IMAGE, images);
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra(KEY_NEED_SAVE, needSaveLocal);
        intent.putExtra(KEY_COOKIE, needCookie);
        context.startActivity(intent);
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        mImageSources = bundle.getStringArray(KEY_IMAGE);
        mCurPosition = bundle.getInt(KEY_POSITION, 0);
        mNeedSaveLocal = bundle.getBoolean(KEY_NEED_SAVE, true);
        mNeedCookie = bundle.getBoolean(KEY_COOKIE, false);
        if (mImageSources != null) {
            // 初始化下载状态
            mImageDownloadStatus = new boolean[mImageSources.length];
            return true;
        }

        return false;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mImagePager = (PreviewerViewPager) findViewById(R.id.vp_image);
        mIndexText = (TextView) findViewById(R.id.tv_index);
        mImagePager.addOnPageChangeListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        int len = mImageSources.length;
        if (len == 1) {
            mIndexText.setVisibility(View.GONE); // 只有一张图片,则不显示数量
        }

        mImagePager.setAdapter(new ViewPagerAdapter());
        mImagePager.setCurrentItem(mCurPosition);

        // First we call to init the TextView
        onPageSelected(mCurPosition);
    }

    /**
     * 切换图片, 更新页码
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        mCurPosition = position; // 更新当前页码
        mIndexText.setText(String.format("%s/%s", (position + 1), mImageSources.length));
        // 滑动时自动切换当前的下载状态, 显示"保存"按钮
        changeSaveButtonStatus(mImageDownloadStatus[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 更新图片下载状态
     *
     * @param pos
     * @param isOk
     */
    private void updateDownloadStatus(int pos, boolean isOk) {
        mImageDownloadStatus[pos] = isOk; // 第 pos 张图片下载状态更新

        // 如果为当前图片, 则更新界面
        if (mCurPosition == pos) {
            changeSaveButtonStatus(isOk);
        }
    }

    /**
     * 更新下载状态到界面,显示"保存到本地"按钮
     *
     * @param isShow
     */
    private void changeSaveButtonStatus(boolean isShow) {
        if (mNeedSaveLocal) {
            findViewById(R.id.iv_save).setVisibility(isShow ? View.VISIBLE : View.GONE);
        } else {
            findViewById(R.id.iv_save).setVisibility(View.GONE); // 禁止保存本地, 则始终不显示保存按钮
        }
    }

    /**
     * 响应保存按钮动作
     */
    @OnClick(R.id.iv_save)
    public void saveToFileByPermission() {
        Toast.makeText(AppContext.context(), "保存到相册", Toast.LENGTH_SHORT).show();
    }

    /**
     * 图片预览 viewPager 适配器, 对应每一张图片布局
     */
    private class ViewPagerAdapter extends PagerAdapter {

        private View.OnClickListener mFinishClickListener;

        @Override
        public int getCount() {
            return mImageSources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.lay_gallery_page_item_container, container, false);
            ImagePreviewView previewView = (ImagePreviewView) view.findViewById(R.id.iv_preview);
            ImageView defaultView = (ImageView) view.findViewById(R.id.iv_default);
            Loading loading = (Loading) view.findViewById(R.id.loading);

            // 加载图片
            if (mNeedCookie) {

            } else {
                loadImage(position, mImageSources[position], previewView, defaultView, loading);
            }

            previewView.setOnClickListener(getListener());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 点击图片, finish activity
         *
         * @return
         */
        private View.OnClickListener getListener() {
            if (mFinishClickListener == null) {
                mFinishClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                };
            }
            return mFinishClickListener;
        }

        /**
         * 网络加载图片
         *
         * @param pos
         * @param urlOrPath
         * @param previewView
         * @param defaultView
         * @param loading
         * @param <T>
         */
        private <T> void loadImage(final int pos, final T urlOrPath,
                                   final ImageView previewView,
                                   final ImageView defaultView,
                                   final Loading loading) {
            loadImageDoDownAndGetOverrideSize(urlOrPath, new DoOverrideSizeCallback() {
                @Override
                public void onDone(int overrideW, int overrideH, boolean isTrue) {
                    // Glide 加载图片
                    DrawableRequestBuilder builder = getImageLoader().load(urlOrPath)
                            // 监听图片加载动作
                            .listener(new RequestListener<T, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e,
                                                           T model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFirstResource) {
                                    if (e != null) {
                                        e.printStackTrace();
                                    }

                                    loading.stop();
                                    loading.setVisibility(View.GONE);
                                    defaultView.setVisibility(View.VISIBLE);
                                    updateDownloadStatus(pos, false);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource,
                                                               T model,
                                                               Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache,
                                                               boolean isFirstResource) {
                                    loading.stop();
                                    loading.setVisibility(View.GONE);
                                    updateDownloadStatus(pos, true);
                                    return false;
                                }
                            }).diskCacheStrategy(DiskCacheStrategy.SOURCE); // 图片缓存策略

                    builder.into(previewView);
                }
            });
        }

        private <T> void loadImageDoDownAndGetOverrideSize(final T urlOrPath,
                                                           final DoOverrideSizeCallback callback) {
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    // Init override size
                    final int overrideW, overrideH;
                    overrideW = 200;
                    overrideH = 200;

                    // Call back on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(overrideW, overrideH, true);
                        }
                    });
                }
            });
        }
    }

    interface DoOverrideSizeCallback {
        void onDone(int overrideW, int overrideH, boolean isTrue);
    }
}
