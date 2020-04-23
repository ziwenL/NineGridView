package com.ziwenl.ninegridview.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ziwenl.ninegridview.R;
import com.ziwenl.ninegridview.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Ziwen Lan
 * Date : 2020/3/30
 * Time : 14:43
 * Introduction :
 * 仿钉钉圈子九宫图样式
 * 该view继承于CardView以便于实现四周圆角效果，并在初始化时取消掉了cardView自带的阴影效果
 * 最多加载九张图片，超过九张只显示前九张
 */
public class NineGirdView extends CardView {
    /**
     * 间隔
     */
    private int mSpace;
    /**
     * 多余的间隔
     */
    private int mOverSpace;
    /**
     * 最多加载九张图片，超过九张只显示前九张
     */
    private List<String> mUrls = new ArrayList<>();
    private List<ImageView> mImageViews = new ArrayList<>();
    /**
     * 单张大图
     */
    private ImageView mBigImageView;
    /**
     * 九张图片时的ImageView宽度，定为基础长度
     */
    private int mBaseLength;

    /**
     * 当前view实际展示宽度(已减去左右padding)
     */
    private int mMeasuredShowWidth;
    private int mPaddingTop, mPaddingLeft, mPaddingRight, mPaddingBottom;


    public NineGirdView(Context context) {
        this(context, null);
    }

    public NineGirdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGirdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //取消阴影效果
        setCardElevation(0f);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGirdView, defStyleAttr,
                R.style.CardView);
        mSpace = typedArray.getDimensionPixelOffset(R.styleable.NineGirdView_pictureSpace, 0);
        int bigPictureMaxHeight = typedArray.getDimensionPixelOffset(R.styleable.NineGirdView_bigPictureMaxHeight, 500);
        typedArray.recycle();

        mPaddingTop = getContentPaddingTop();
        mPaddingLeft = getContentPaddingLeft();
        mPaddingRight = getContentPaddingRight();
        mPaddingBottom = getContentPaddingBottom();

        /*
         * 添加图片view
         */
        //九张小图
        for (int i = 0; i < 9; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final int position = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickPictureListener(position, mUrls);
                    }
                }
            });
            addView(imageView);
            mImageViews.add(imageView);
        }
        //单张大图
        mBigImageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mBigImageView.setLayoutParams(layoutParams);
        mBigImageView.setAdjustViewBounds(true);
        mBigImageView.setMaxHeight(bigPictureMaxHeight);
        mBigImageView.setScaleType(ImageView.ScaleType.FIT_START);
        mBigImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickPictureListener(0, mUrls);
                }
            }
        });
        addView(mBigImageView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得父View提供的可绘制宽高(含padding)
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (mUrls.size() == 0) {
            setMeasuredDimension(widthSize, getRealHeight());
        } else if (mUrls.size() == 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            //绘制子view宽高
            measureChildView(widthSize);
            setMeasuredDimension(widthSize, getRealHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mUrls.size() != 1 && mUrls.size() != 0) {
            //摆放子view位置
            layoutChildView();
        }
    }

    /**
     * 根据子view获得真实高度
     */
    private int getRealHeight() {
        int realHeight = 0;
        switch (mUrls.size()) {
            default:
            case 0:
            case 1:
                break;
            case 2:
                realHeight = ((mMeasuredShowWidth - mSpace) / 2);
                break;
            case 3:
                realHeight = mSpace + (mBaseLength * 2);
                break;
            case 4:
                realHeight = mMeasuredShowWidth;
                break;
            case 5:
                realHeight = ((mMeasuredShowWidth - mSpace) / 2) + mSpace + mBaseLength;
                break;
            case 6:
                realHeight = (mBaseLength * 3) + (2 * mSpace);
                break;
            case 7:
                realHeight = (2 * mSpace) + (int) (3.5 * mBaseLength);
                break;
            case 8:
                realHeight = ((mMeasuredShowWidth - mSpace) / 2) + (2 * mSpace) + (2 * mBaseLength);
                break;
            case 9:
                realHeight = (mBaseLength * 3) + (mSpace * 2);
                break;
        }
        realHeight = realHeight + mPaddingTop + mPaddingBottom;
        return realHeight;
    }

    /**
     * 计算子View宽高
     *
     * @param widthSize 当前view可使用的最大绘制宽度(含padding)
     */
    private void measureChildView(int widthSize) {
        //当前View实际可展示内容宽度
        mMeasuredShowWidth = widthSize - this.mPaddingLeft - mPaddingRight;
        //九宫图单张图片基本长度
        mBaseLength = (mMeasuredShowWidth - (2 * mSpace)) / 3;
        mOverSpace = (mMeasuredShowWidth - (2 * mSpace)) % 3;
        switch (mUrls.size()) {
            default:
            case 0:
            case 1:
                break;
            case 2:
                int length = (mMeasuredShowWidth - mSpace) / 2;
                mOverSpace = (mMeasuredShowWidth - mSpace) % 2;
                for (int i = 0; i < getChildCount(); i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i < 2) {
                        width = length;
                        height = length;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 3:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i == 0) {
                        width = mMeasuredShowWidth - mSpace - mBaseLength;
                        height = (2 * mBaseLength) + mSpace;
                    } else if (i < 3) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 4:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i == 0) {
                        width = mMeasuredShowWidth;
                        height = mMeasuredShowWidth - mSpace - mBaseLength;
                    } else if (i < 4) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 5:
                mOverSpace = (mMeasuredShowWidth - mSpace) % 2;
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i < 2) {
                        width = (mMeasuredShowWidth - mSpace) / 2;
                        height = (mMeasuredShowWidth - mSpace) / 2;
                    } else if (i < 5) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 6:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i == 0) {
                        width = mMeasuredShowWidth - mSpace - mBaseLength;
                        height = (2 * mBaseLength) + mSpace;
                    } else if (i < 6) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 7:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i == 0) {
                        width = mMeasuredShowWidth;
                        height = (int) (1.5 * mBaseLength);
                    } else if (i < 7) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 8:
                mOverSpace = (mMeasuredShowWidth - mSpace) % 2;
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = 0;
                    int height = 0;
                    if (i < 2) {
                        width = (mMeasuredShowWidth - mSpace) / 2;
                        height = (mMeasuredShowWidth - mSpace) / 2;
                    } else if (i < 8) {
                        width = mBaseLength;
                        height = mBaseLength;
                    }
                    childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                }
                break;
            case 9:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    childView.measure(MeasureSpec.makeMeasureSpec(mBaseLength, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(mBaseLength, MeasureSpec.EXACTLY));
                }
                break;
        }
    }

    /**
     * 子View位置摆放
     */
    private void layoutChildView() {
        //布局时要考虑 padding
        switch (mUrls.size()) {
            default:
            case 0:
            case 1:
                break;
            case 2:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 2) {
                        int width = childView.getMeasuredWidth();
                        childView.layout(mPaddingLeft + (i * (width + mSpace + mOverSpace)),
                                mPaddingTop,
                                mPaddingLeft + (i * (width + mSpace + mOverSpace)) + width,
                                mPaddingTop + width);
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 3:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 3) {
                        int width = childView.getMeasuredWidth();
                        int height = childView.getMeasuredHeight();
                        if (i == 0) {
                            childView.layout(mPaddingLeft, mPaddingTop, width + mPaddingLeft, mPaddingTop + height);
                        } else {
                            int count = i - 1;
                            childView.layout(mPaddingLeft + mMeasuredShowWidth - width,
                                    mPaddingTop + (count * mSpace) + (count * height),
                                    mPaddingLeft + mMeasuredShowWidth,
                                    mPaddingTop + ((count + 1) * height) + (count * mSpace));
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 4:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 4) {
                        int width = childView.getMeasuredWidth();
                        int height = childView.getMeasuredHeight();
                        if (i == 0) {
                            childView.layout(mPaddingLeft, mPaddingTop, width + mPaddingLeft, mPaddingTop + height);
                        } else {
                            int count = i - 1;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mMeasuredShowWidth - mBaseLength + mPaddingTop,
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mMeasuredShowWidth + mPaddingTop);
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 5:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 5) {
                        int width = childView.getMeasuredWidth();
                        if (i < 2) {
                            childView.layout(mPaddingLeft + (i * (width + mSpace + mOverSpace)),
                                    mPaddingTop,
                                    mPaddingLeft + (i * (width + mSpace + mOverSpace)) + width,
                                    mPaddingTop + width);
                        } else {
                            int count = i - 2;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + mSpace,
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + mSpace + width);
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 6:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 6) {
                        int width = childView.getMeasuredWidth();
                        int height = childView.getMeasuredHeight();
                        if (i == 0) {
                            childView.layout(mPaddingLeft, mPaddingTop, mPaddingLeft + width, height + mPaddingTop);
                        } else if (i < 3) {
                            int count = i - 1;
                            childView.layout(mPaddingLeft + mMeasuredShowWidth - width,
                                    mPaddingTop + (count * mSpace) + (count * height),
                                    mPaddingLeft + mMeasuredShowWidth,
                                    mPaddingTop + ((count + 1) * height) + (count * mSpace));
                        } else {
                            int count = i - 3;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + (2 * mSpace) + (2 * mBaseLength),
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + mMeasuredShowWidth);
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 7:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 7) {
                        int width = childView.getMeasuredWidth();
                        int height = childView.getMeasuredHeight();
                        if (i == 0) {
                            childView.layout(mPaddingLeft, mPaddingTop, mPaddingLeft + width, height + mPaddingTop);
                        } else if (i < 4) {
                            int count = i - 1;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + (int) (1.5 * height) + mSpace,
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + (int) (2.5 * height) + mSpace);
                        } else {
                            int count = i - 4;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + (int) (2.5 * height) + (2 * mSpace),
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + (int) (3.5 * height) + (2 * mSpace));
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 8:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    if (i < 8) {
                        int width = childView.getMeasuredWidth();
                        if (i < 2) {
                            childView.layout(mPaddingLeft + (i * (width + mSpace + mOverSpace)),
                                    mPaddingTop,
                                    mPaddingLeft + (i * (width + mSpace + mOverSpace)) + width,
                                    mPaddingTop + width);
                        } else if (i < 5) {
                            int count = i - 2;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + mSpace,
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + mSpace + width);
                        } else {
                            int count = i - 5;
                            childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + width + (2 * mSpace),
                                    mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                    mPaddingTop + ((mMeasuredShowWidth - mSpace) / 2) + (2 * width) + (2 * mSpace));
                        }
                    } else {
                        childView.layout(0, 0, 0, 0);
                    }
                }
                break;
            case 9:
                for (int i = 0; i < getChildCount() - 1; i++) {
                    View childView = getChildAt(i);
                    int width = childView.getMeasuredWidth();
                    int height = childView.getMeasuredHeight();
                    if (i < 3) {
                        childView.layout(mPaddingLeft + (i * mSpace) + (i * width),
                                mPaddingTop,
                                mPaddingLeft + (i * mSpace) + ((i + 1) * width),
                                mPaddingTop + height);
                    } else if (i < 6) {
                        int count = i - 3;
                        childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                mPaddingTop + height + mSpace,
                                mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                mPaddingTop + (2 * height) + mSpace);
                    } else {
                        int count = i - 6;
                        childView.layout(mPaddingLeft + (count * mSpace) + (count * width),
                                mPaddingTop + (2 * height) + (2 * mSpace),
                                mPaddingLeft + (count * mSpace) + ((count + 1) * width),
                                mPaddingTop + (3 * height) + (2 * mSpace));
                    }
                }
                break;
        }
    }


    /**
     * 填充数据
     */
    public void setUrls(List<String> urls) {
        int currentCount = mUrls.size();
        int newCount = urls == null || urls.isEmpty() ? 0 : urls.size();

        mUrls.clear();
        if (urls != null && !urls.isEmpty()) {
            mUrls.addAll(urls);
        }
        if (mUrls.size() > 9) {
            mUrls = mUrls.subList(0, 9);
        }
        //数据量存在变化时才重新调整布局
        if (currentCount != newCount) {
            //复原
            if (mUrls.size() == 0) {
                getLayoutParams().width = LayoutParams.MATCH_PARENT;
                mBigImageView.setVisibility(GONE);
                for (ImageView imageView : mImageViews) {
                    imageView.setVisibility(GONE);
                }
            } else if (mUrls.size() == 1) {
                getLayoutParams().width = LayoutParams.WRAP_CONTENT;
                for (ImageView imageView : mImageViews) {
                    imageView.setVisibility(GONE);
                }
                mBigImageView.setVisibility(VISIBLE);
            } else {
                getLayoutParams().width = LayoutParams.MATCH_PARENT;
                mBigImageView.setVisibility(GONE);
                for (int i = 0; i < mImageViews.size(); i++) {
                    if (i < mUrls.size()) {
                        mImageViews.get(i).setVisibility(VISIBLE);
                    } else {
                        mImageViews.get(i).setVisibility(GONE);
                    }
                }
            }
        }

        if (mUrls.size() == 1) {
            //TODO 填充网络图片
            GlideUtil.loadPicture(mUrls.get(0), mBigImageView);
        } else {
            for (int i = 0; i < mUrls.size(); i++) {
                ImageView imageView = mImageViews.get(i);
                //TODO 填充网络图片
                GlideUtil.loadPicture(mUrls.get(i), imageView);
            }
        }
    }


    //--------  点击事件回调  --------

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onClickPictureListener(int position, List<String> urls);
    }
}
