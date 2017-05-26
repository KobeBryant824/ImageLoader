package com.example.cxh.imageloadersample.imageloader;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.example.cxh.imageloadersample.util.FileUtils;
import com.example.cxh.imageloadersample.util.MD5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * 本地缓存
 * Created by Hai (haigod7@gmail.com) on 2017/3/6 10:51.
 */
public class LocalCacheUtils {
    private final String CACHE_DIR = FileUtils.getCacheDir();

    /**
     * 添加图片到磁盘
     *
     * @param imgUrl 图片地址
     * @param bitmap Bitmap
     */
    public void putBitmap(String imgUrl, Bitmap bitmap) {
        if (getBitmap(imgUrl) == null) {
            try {
                String fileName = MD5Utils.encrypt(imgUrl);
                File file = new File(CACHE_DIR, fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取本地图片
     *
     * @param imgUrl 图片地址
     * @return Bitmap
     */
    public Bitmap getBitmap(String imgUrl) {
        try {
            String fileName = MD5Utils.encrypt(imgUrl);
            File file = new File(CACHE_DIR, fileName);
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
