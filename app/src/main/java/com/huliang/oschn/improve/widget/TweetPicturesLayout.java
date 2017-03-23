package com.huliang.oschn.improve.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.bean.Tweet;
import com.huliang.oschn.improve.media.ImageGalleryActivity;
import com.huliang.oschn.util.TLog;

/**
 * Created by huliang on 17/3/20.
 */
public class TweetPicturesLayout extends ViewGroup implements View.OnClickListener {
    private static final int SINGLE_MAX_W = 120;
    private static final int SINGLE_MAX_H = 180;
    private static final int SINGLE_MIN_W = 34;
    private static final int SINGLE_MIN_H = 34;

    private Tweet.Image[] mImages;
    private float mVerticalSpacing;
    private float mHorizontalSpacing;
    private int mColumn;
    private int mMaxPictureSize;

    public TweetPicturesLayout(Context context) {
        this(context, null);
    }

    public TweetPicturesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetPicturesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TweetPicturesLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 获取自定义属性值
     *
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final Context context = getContext();

        final float density = getResources().getDisplayMetrics().density;
        // 默认高和宽
        int vSpace = (int) (4 * density);
        int hSpace = vSpace;

        if (attrs != null) {
            // Load attributes 获取自定义属性的值
            final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.TweetPicturesLayout, defStyleAttr, defStyleRes);

            // Load clip touch corner radius
            vSpace = typedArray.getDimensionPixelOffset(R.styleable.TweetPicturesLayout_verticalSpace,
                    vSpace);
            hSpace = typedArray.getDimensionPixelOffset(R.styleable.TweetPicturesLayout_horizontalSpace,
                    hSpace);
            int c = typedArray.getInt(R.styleable.TweetPicturesLayout_column, 3);
            int m = typedArray.getInt(R.styleable.TweetPicturesLayout_maxPictureSize, 0);
            setColumn(c);
            setMaxPictureSize(m);
            typedArray.recycle(); // 回收并复用
        }

        setVerticalSpacing(vSpace);
        setHorizontalSpacing(hSpace);
    }

    public void setColumn(int column) {
        if (column < 1) {
            column = 1;
        }
        if (column > 20) {
            column = 20;
        }
        this.mColumn = column;
    }

    public void setHorizontalSpacing(float pixelSize) {
        this.mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(float pixelSize) {
        this.mVerticalSpacing = pixelSize;
    }

    public void setMaxPictureSize(int maxPictureSize) {
        if (maxPictureSize < 0) {
            maxPictureSize = 0;
        }
        this.mMaxPictureSize = maxPictureSize;
    }

    public void setImages(Tweet.Image[] images) {
        if (mImages == images) {
            return;
        }

        // 移除布局
        removeAllImage();

        // 过滤掉不合法的数据

        // 赋值
        mImages = images;

        if (mImages != null && mImages.length > 0) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            RequestManager requestManager = Glide.with(getContext());

            // 遍历images,分别加载并添加到本视图TweetPicturesLayout
            for (int i = 0; i < mImages.length; i++) {
                Tweet.Image image = mImages[i];
                if (!Tweet.Image.check(image)) {
                    continue;
                }

                // 从布局文件创建视图框架
                View view = inflater.inflate(R.layout.lay_tweet_image_item, this, false);
                view.setTag(i);
                view.setOnClickListener(this);
                String path = image.getThumb();
                BitmapRequestBuilder builder = requestManager.load(path)
                        .asBitmap()
                        .centerCrop() // 均衡缩放图像（保持图像原始比例）使图片的两个坐标（宽、高）都大于等于相应的视图坐标
                        .error(R.mipmap.ic_split_graph);
                addView(view); // 添加子控件到本视图
                builder.into((ImageView) view.findViewById(R.id.iv_picture));
            }

            // all do requestLayout
            if (getVisibility() == VISIBLE) {
                // 当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent view（父类的视图）
                // 重新调用他的onMeasure, onLayout来重新设置自己位置。
                requestLayout();
            } else {
                setVisibility(VISIBLE);
            }
        } else {
            // 没有图片,则移除本视图
            setVisibility(GONE);
        }
    }

    /**
     * 移除所有子控件
     */
    public void removeAllImage() {
        removeAllViews();
        mImages = null;
    }

    /**
     * 返回 mMaxPictureSize 和当前 size 中的较小者
     *
     * @param size
     * @return
     */
    private int getMaxChildSize(int size) {
        if (mMaxPictureSize == 0) {
            return size;
        } else {
            return Math.min(mMaxPictureSize, size);
        }
    }

    /**
     * 重写onMeasure方法来对控件进行测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        // 根据提供的大小和模式，返回你想要的大小值
        int selfWidth = resolveSize(paddingLeft + paddingRight, widthMeasureSpec);
        int wantedHeight = paddingTop + paddingBottom;

        final int childCount = getChildCount(); // 子控件的个数

        // no inspection Statement With Empty Body
        if (childCount == 0) {
            // Not have child we can only need padding size
        } else if (childCount == 1) {
            Tweet.Image image = mImages[0];
            if (Tweet.Image.check(image)) {
                int imageW = image.getW();
                int imageH = image.getH();

                // 如果图片大小未设置,则默认为100
                imageW = imageW <= 0 ? 100 : imageW;
                imageH = imageH <= 0 ? 100 : imageH;

                float density = getResources().getDisplayMetrics().density;
                // 获取可显示区域的最大宽和高
                float maxContentW = Math.min(selfWidth - paddingLeft - paddingRight, density * SINGLE_MAX_W);
                float maxContentH = density * SINGLE_MAX_H;

                int childW, childH;
                float h2wRatio = imageH / (float) imageW; // 原图尺寸比例

                // 图片最大尺寸: 可视区域,同时要按照原图片尺寸比例缩小
                if (h2wRatio > maxContentH / maxContentW) {
                    childH = (int) maxContentH;
                    childW = (int) (maxContentH / h2wRatio);
                } else {
                    childW = (int) maxContentW;
                    childH = (int) (maxContentW * h2wRatio);
                }

                // 图片最小尺寸: 1:1默认正方形大小
                int minW = (int) (density * SINGLE_MIN_W);
                if (childW < minW) {
                    childW = minW;
                }
                int minH = (int) (density * SINGLE_MIN_H);
                if (childH < minH) {
                    childH = minH;
                }

                View childView = getChildAt(0);
                if (childView != null) {
                    // makeMeasureSpec: 根据提供的大小值和模式创建一个测量值
                    childView.measure(
                            MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY));
                    wantedHeight += childH;
                }
            }
        } else {
            // Measure all children
            // 获取可显示区域的最大宽(多图情况下图片为正方形)
            final float maxContentWidth = selfWidth - paddingLeft - paddingRight -
                    mHorizontalSpacing * (mColumn - 1);
            // Get child size
            // 每张图片的最大边长
            final int childSize = getMaxChildSize((int) (maxContentWidth / mColumn));

            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // makeMeasureSpec: 根据提供的大小值和模式创建一个测量值
                childView.measure(
                        MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY));
            }

            wantedHeight += childSize;
        }

        // 传递View的高度和宽度到setMeasuredDimension方法,告诉父控件需要多大地方放置子控件
        setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount(); // 子控件的个数
        if (childCount > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();

            if (childCount == 1) {
                View childView = getChildAt(0);
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(paddingLeft, paddingTop, paddingLeft + childWidth,
                        paddingTop + childHeight);
            } else {
                // 多图模式, 初始化"原点"坐标
                int childLeft = paddingLeft;
                int childTop = paddingTop;

                for (int i = 0; i < childCount; i++) {
                    View childView = getChildAt(i);
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();
                    childView.layout(childLeft, childTop, childLeft + childWidth,
                            childTop + childHeight);

                    // 更新下一张图片的起点坐标
                    childLeft += childWidth + mHorizontalSpacing;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Tweet.Image[] images = mImages;
        if (images == null || images.length <= 0) {
            return;
        }

        Object obj = v.getTag();
        if (obj == null || !(obj instanceof Integer)) {
            return;
        }

        // 被点击图片的位置
        int index = (int) obj;
        if (index < 0) {
            index = 0;
        } else if (index >= images.length) {
            index = images.length - 1;
        }

        // 被点击图片
        Tweet.Image image = images[index];
        if (!Tweet.Image.check(image)) {
            return;
        }

        // 图片列表的urls
        String[] paths = Tweet.Image.getImagePaths(images);
        if (paths == null || paths.length == 0) {
            return;
        }

        ImageGalleryActivity.show(getContext(), paths, index);
    }
}
