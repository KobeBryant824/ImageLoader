package com.example.cxh.imageloadersample.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.cxh.imageloadersample.R;

import java.io.File;

/**
 * Glide图片加载
 * Created by Hai (haigod7@gmail.com) on 2017/3/10 10:40.
 */
public class GlideUtils {

    /**
     * 特殊path用此方法加载
     */
    public static void loadImgAny(Object obj, ImageView iv) {
        Context context = iv.getContext();
        RequestManager manager = Glide.with(context);
        DrawableTypeRequest drawableTypeRequest = null;

        if (obj instanceof String) {
            drawableTypeRequest = manager.load((String) obj);
        } else if (obj instanceof Integer) {
            drawableTypeRequest = manager.load((Integer) obj);
        } else if (obj instanceof Uri) {
            drawableTypeRequest = manager.load((Uri) obj);
        } else if (obj instanceof File) {
            drawableTypeRequest = manager.load((File) obj);
        }
        if (drawableTypeRequest == null) return;

        drawableTypeRequest.centerCrop().into(iv);
    }

    /**
     * 默认加载
     */
    public static void loadImage(String path, ImageView imageView) {
        loadImage(path, R.drawable.ic_placeholder, R.mipmap.ic_launcher, imageView);

//        Context context = imageView.getContext();
//        Glide.with(context)
//                .load(path)
//                .centerCrop()
//                .into(imageView);
    }

    /**
     * 设置加载中以及加载失败图片
     */
    public static void loadImage(String path, @DrawableRes int loadingImageView, @DrawableRes int errorImageView, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .centerCrop()
                .placeholder(loadingImageView)
                .error(errorImageView)
                .into(imageView);
    }

    /**
     * 设置缩略图支持，会先加载缩略图
     */
    public static void loadImage(String path, float sizeMultiplier, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .centerCrop()
                .thumbnail(sizeMultiplier)
                .into(imageView);
    }

    /**
     * 设置下载优先级
     *
     * @param path
     * @param priority  IMMEDIATE 立即, HIGH, NORMAL 默认, LOW, priority
     * @param imageView
     */
    public static void loadImage(String path, Priority priority, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .priority(priority)
                .into(imageView);
    }

    /**
     * 设置磁盘缓存策略
     *
     * @param path
     * @param strategy  all:缓存源资源和转换后的资源
     *                  none:不作任何磁盘缓存
     *                  source:缓存源资源
     *                  result：缓存转换后的资源，默认的
     * @param imageView
     */
    public static void loadImage(String path, DiskCacheStrategy strategy, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(strategy)
                .into(imageView);
    }

    /**
     * 设置监听请求接口
     *
     * @param path
     * @param imageView
     * @param requestListener 用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
     */
    public static void loadImage(String path, ImageView imageView, RequestListener<String, GlideDrawable> requestListener) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .centerCrop()
                .listener(requestListener)
                .into(imageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    /**
     * 设置要加载的内容
     */
    public static void loadImage(Context context, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(context)
                .load(path)
                .centerCrop()
                .into(simpleTarget);
    }

    /**
     * 加载圆形图片
     */
    public static void loadRoundImage(String path, final ImageView imageView) {
        final Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 自定义宽高
     */
    public static void loadOverrideImage(String path, int width, int height, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .centerCrop()
                .override(width, height)
                .into(imageView);
    }

    /**
     * 设置动态GIF加载方式，如果不是gif会回调error
     */
    public static void loadGif(String path, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // Source 及 None ,其他或者不添加策略会非常慢、卡
                .centerCrop()
                .into(imageView);
    }

    /**
     * 设置静态加载方式，获取第一帧
     */
    public static void loadStaticGif(String path, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .asBitmap()
                .centerCrop()
                .into(imageView);
    }

    /**
     * 设置跳过内存缓存
     */
    public static void loadImageSkipCache(String path, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(path)
                .centerCrop()
                .skipMemoryCache(true)
                .into(imageView);
    }

    /**
     * 清理磁盘缓存，需要在子线程中执行
     */
    public static void GuideClearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清理内存缓存，可以在UI主线程中进行
     */
    public static void GuideClearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

}