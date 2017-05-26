package com.example.cxh.imageloadersample.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * 自定义图片加载，2级缓存，1级网络
 * Created by Hai (haigod7@gmail.com) on 2017/3/6 10:51.
 */
public class ImageLoader {
    private MemoryCacheUtils mCacheUtils;
    private LocalCacheUtils lCacheUtils;
    private NetCacheUtils nCacheUtils;

    public ImageLoader(Handler handler) {
        mCacheUtils = new MemoryCacheUtils();
        lCacheUtils = new LocalCacheUtils();
        nCacheUtils = new NetCacheUtils(handler, lCacheUtils, mCacheUtils);
    }

    /**
     * 根据Url取图片
     *
     * @param imageUrl 图片地址
     * @param position 如果是列表，则为position，否则-1
     * @return Bitmap
     */
    public Bitmap getBitmap(String imageUrl, int position) {
        // 1. 先根据Url去内存中取, 如果取到直接返回。
        Bitmap bitmap = mCacheUtils.getBitmap(imageUrl);
        if (bitmap != null) {
            System.out.println("从内存中取到的");
            return bitmap;
        }

        // 2. 再去本地取, 如果取到直接返回。
        bitmap = lCacheUtils.getBitmap(imageUrl);
        if (bitmap != null) {
            System.out.println("从本地中取到的");
            return bitmap;
        }

        // 3. 请求网络从服务器取, 取到之后发送给主线程显示。
        System.out.println("从网络中取到的");
        nCacheUtils.requestBitmap(imageUrl, position);
        return null;
    }

}