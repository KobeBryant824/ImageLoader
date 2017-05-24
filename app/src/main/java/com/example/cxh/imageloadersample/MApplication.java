package com.example.cxh.imageloadersample;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Desc:
 * Created by Hai (haigod7@gmail.com) on 2017/5/23 13:44.
 */
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(this)
//                .setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "fresco"))
                .setBaseDirectoryPath(new File(getExternalCacheDir(), "fresco"))
                .setBaseDirectoryName("fresco_sample")
                .setMaxCacheSize(100 * 1024 * 1024)//100MB
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .setCacheKeyFactory(cacheKeyFactory)
//                .setDownsampleEnabled(true)
//                .setWebpSupportEnabled(true)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();
        Fresco.initialize(this , config);

    }
}
