package com.ziwenl.ninegridview.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Author : Ziwen Lan
 * Date : 2019/10/11
 * Time : 17:00
 * Introduction : Glide工具类
 */
public class GlideUtil {
    private static final String TAG = GlideUtil.class.getSimpleName();

    /**
     * Glide的请求管理器类
     */
    private static RequestManager mRequestManager;
    private static Context mContext;
    /**
     * 默认占位图
     * 默认占位头像
     * 默认圆形图
     */
    private static RequestOptions mDefaultImgOptions;
    private static RequestOptions mDefaultAvatarOptions;
    private static RequestOptions mDefaultRoundImgOptions;

    /**
     * 初始化Glide工具
     */
    public static void init(Context context) {
        if (mContext == null) {
            mContext = context.getApplicationContext();
            mRequestManager = Glide.with(context);
            mDefaultImgOptions = new RequestOptions();
            mDefaultRoundImgOptions = new RequestOptions().circleCrop();
            mDefaultAvatarOptions = new RequestOptions().circleCrop();

        }
    }

    /**
     * 初始化Glide工具并设置默认占位图和头像
     *
     * @param defaultImg    默认占位图资源id
     * @param defaultAvatar 默认占位头像资源id
     */
    public static void init(Context context, @DrawableRes int defaultImg, @DrawableRes int defaultAvatar) {
        if (mContext == null) {
            mContext = context.getApplicationContext();
            mRequestManager = Glide.with(context);
            if (defaultImg != -1) {
                mDefaultImgOptions = new RequestOptions()
                        .placeholder(defaultImg);
                mDefaultRoundImgOptions = new RequestOptions()
                        .placeholder(defaultImg)
                        .circleCrop();

            } else {
                mDefaultImgOptions = new RequestOptions();
                mDefaultRoundImgOptions = new RequestOptions()
                        .circleCrop();
            }
            if (defaultAvatar == -1) {
                mDefaultAvatarOptions = new RequestOptions()
                        .circleCrop();
            } else {
                mDefaultAvatarOptions = new RequestOptions()
                        .placeholder(defaultAvatar)
                        .circleCrop();
            }
        }
    }

    /**
     * Glide工具类是否已经初始化
     *
     * @return 已初始化则返回true
     */
    private static boolean isInit() {
        if (mContext == null || mRequestManager == null) {
            Log.e(TAG, TAG + "not init");
            return false;
        }
        return true;
    }

    /**
     * 加载正方形的网络图片
     *
     * @param url       网络地址
     * @param imageView 目标控件
     */
    public static void loadPicture(String url, ImageView imageView) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        mRequestManager
                .load(url)
                .apply(mDefaultImgOptions)
                .into(imageView);
    }

    /**
     * 加载正方形的网络图片
     *
     * @param url       网络地址
     * @param imageView 目标控件
     */
    public static void loadPicture(String url, ImageView imageView, int width, int height) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .override(width,height);
        mRequestManager.load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载正方形的网络图片
     *
     * @param url        网络地址
     * @param imageView  目标控件
     * @param defaultImg 默认的图片 若不需要则输入-1
     */
    public static void loadPicture(String url, ImageView imageView, @DrawableRes int defaultImg) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        if (defaultImg == -1) {
            mRequestManager.load(url)
                    .into(imageView);
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(defaultImg);
            mRequestManager.load(url)
                    .apply(options)
                    .into(imageView);
        }
    }


    /**
     * 加载头像(已裁剪成圆形)
     *
     * @param url       网络地址
     * @param imageView 目标控件
     */
    public static void loadAvatarPicture(String url, ImageView imageView) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        mRequestManager.load(url)
                .apply(mDefaultAvatarOptions)
                .into(imageView);
    }

    /**
     * 加载头像(已裁剪成圆形)
     *
     * @param url        网络地址
     * @param imageView  目标控件
     * @param defaultImg 占位图 -1表示不设置
     */
    public static void loadAvatarPicture(String url, ImageView imageView, @DrawableRes int defaultImg) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .circleCrop();
        if (defaultImg != -1) {
            options = options.placeholder(defaultImg);
        }
        mRequestManager.load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载网络图片并设置圆角
     *
     * @param url                  网络图片地址
     * @param imageView            如果imageView设置了scaleType，可能会导致圆角无效，所以scaleType请通过bitmapTransformation入参来设置
     * @param px                   圆角值
     * @param bitmapTransformation 图片填充类型 传null表示不设置 eg: new CenterCrop()/new FitCenter()
     * @param defaultImage         占位图
     */
    public static void loadRoundPicture(String url, ImageView imageView, int px, BitmapTransformation bitmapTransformation, @DrawableRes int defaultImage) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true);
        if (defaultImage == -1) {
            options = options.placeholder(mDefaultImgOptions.getPlaceholderId());
        } else {
            options = options.placeholder(defaultImage);
        }
        if (null == bitmapTransformation) {
            options = options.transform(new RoundedCorners(px));
        } else {
            options = options.transforms(bitmapTransformation, new RoundedCorners(px));
        }
        mRequestManager
                .load(url)
                .apply(options)
                .into(imageView);
    }


    /**
     * 加载网络图片并设置圆角
     *
     * @param url                  网络图片地址
     * @param imageView            如果imageView设置了scaleType，可能会导致圆角无效，所以scaleType请通过bitmapTransformation入参来设置
     * @param px                   圆角值
     * @param bitmapTransformation 图片填充类型 传null表示不设置 eg: new CenterCrop()/new FitCenter()
     */
    public static void loadRoundPicture(String url, ImageView imageView, int px, BitmapTransformation bitmapTransformation) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true);
        options = options.placeholder(mDefaultImgOptions.getPlaceholderId());
        if (null == bitmapTransformation) {
            options = options.transform(new RoundedCorners(px));
        } else {
            options = options.transforms(bitmapTransformation, new RoundedCorners(px));
        }
        mRequestManager
                .load(url)
                .apply(options)
                .into(imageView);
    }


    /**
     * 加载圆形图片
     *
     * @param url       网络地址
     * @param imageView 目标控件
     */
    public static void loadRoundPicture(String url, ImageView imageView) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
        mRequestManager.load(url)
                .apply(mDefaultRoundImgOptions)
                .into(imageView);
    }

    /**
     * 加载网络图片并设置监听
     */
//    public static void loadPicture(String url, DrawableImageViewTarget viewTarget) {
//        if (!isInit()) {
//            return;
//        }
//        if (viewTarget == null) {
//            return;
//        }
//        mRequestManager.load(url)
//                .apply(mDefaultAvatarOptions)
//                .into(viewTarget);
//    }
}
