package com.example.cxh.imageloadersample.imageloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * 内存缓存
 * Created by Hai (haigod7@gmail.com) on 2017/3/6 10:51.
 */
public class MemoryCacheUtils {
    private LruCache<String, Bitmap> mLruCache;

    public MemoryCacheUtils() {
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 8; // 使用最大可用内存值的1/8作为缓存的大小

        // LruCache通过构造函数传入缓存值，以KB为单位
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {

            // 重写此方法来衡量每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    /**
     * 向内存中存一张图片
     *
     * @param imgUrl 图片地址
     * @param bitmap Bitmap
     */
    public void putBitmap(String imgUrl, Bitmap bitmap) {
        if (getBitmap(imgUrl) == null) {
            mLruCache.put(imgUrl, bitmap);
        }
    }

    /**
     * 从内存中取一张图片
     *
     * @param imgUrl 图片地址
     * @return Bitmap
     */
    public Bitmap getBitmap(String imgUrl) {
        return mLruCache.get(imgUrl);
    }
}
