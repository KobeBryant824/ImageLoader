package com.example.cxh.imageloadersample.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.socks.library.KLog;

import java.io.File;
import java.io.InputStream;

/**
 * Desc: 配置全局 GlideModule
 * Created by Hai (haigod7@gmail.com) on 2017/5/18 14:43.
 */
public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 13.77 MB --> 12.18 = 1.59MB 一张大图就多了1.5M
        //默认格式RGB_565使用内存是ARGB_8888的一半，但是图片质量就没那么高了，而且不支持透明度
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // Glide加载的大小和ImageView的大小是一致，并且缓存imageview不同尺寸的图片，尽管你的path都一样

        setMemoryCacheSize(context, builder);

        setDiskCache(context, builder);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // 默认缓存图片路径data/data/包名/image_manager_disk_cache,可以自己修改
        KLog.e(Glide.getPhotoCacheDir(context)); // 卸载会有垃圾
//        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH); //调整内存缓存的大小为初始值的0.5,1,1.5
        // 使用OkHttp 做协议栈，默认使用HTTPURLConnection
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    // 设置内存缓存大小
    private void setMemoryCacheSize(Context context, GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一

        //这个类会根据设备屏幕的大小，计算出一个合适的size
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();//差不多是1/8
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();// 差不多是应用内存的1/4

        // memoryCacheSize:16777216,defaultMemoryCacheSize:16588800,defaultBitmapPoolSize:33177600
        KLog.e("memoryCacheSize:" + memoryCacheSize + ",defaultMemoryCacheSize:" + defaultMemoryCacheSize + ",defaultBitmapPoolSize:" + defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));//自定义池子的大小
    }

    // 设置磁盘缓存大小、目录
    private void setDiskCache(Context context, GlideBuilder builder) {
        File cacheDir = context.getExternalCacheDir();//指定数据的缓存地址，而且随应用卸载而删除数据
        // 1M = 1024KB  1KB = 1024B
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据，相当于30M

        //存放在SDCard/Android/data/data/<application package>/cache/, 这两句等同
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", diskCacheSize));

        //存放在data/data/<application package>/cache/
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));

    }
}
