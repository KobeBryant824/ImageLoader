package com.example.cxh.imageloadersample.fresco;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cxh.imageloadersample.R;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.cxh.imageloadersample.MainActivity.BIG_PATH;
import static com.example.cxh.imageloadersample.MainActivity.GIF_PATH;
import static com.example.cxh.imageloadersample.MainActivity.SMALL_PATH;

/**
 * Desc:
 * Created by Hai (haigod7@gmail.com) on 2017/5/23 11:07.
 */
public class FrescoFragment extends Fragment {

    @BindView(R.id.drawee_view0)
    SimpleDraweeView mDraweeView0;
    @BindView(R.id.drawee_view00)
    SimpleDraweeView mDraweeView00;
    @BindView(R.id.drawee_view)
    SimpleDraweeView mDraweeView;
    @BindView(R.id.drawee_view1)
    SimpleDraweeView mDraweeView1;
    @BindView(R.id.drawee_view3)
    SimpleDraweeView mDraweeView3;
    @BindView(R.id.drawee_view4)
    SimpleDraweeView mDraweeView4;
    @BindView(R.id.drawee_view5)
    SimpleDraweeView mDraweeView5;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fresco, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 支持渐进式的网络JPEG图。在开始加载之后，图会从模糊到清晰渐渐呈现。
        // 只支持网络，本地都是一次性加载完成
        Uri uri0 = Uri.parse("http://wallpaper.macappstore.net/d/file/wallpaper/2017/05-14/06fb75fa619630aebc53088a9e9fba4c.jpg"); // 10M大图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri0)
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController controller0 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(mDraweeView0.getController())
                .build();
        mDraweeView0.setController(controller0);

        GenericDraweeHierarchyBuilder builder0 = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy0 = builder0
                .setProgressBarImage(new ProgressBarDrawable()) // 显示深蓝色的矩形进度条
                .build();
        mDraweeView0.setHierarchy(hierarchy0);


        // 假设你要显示一张高分辨率的图，但是这张图下载比较耗时。与其一直显示占位图，你可能想要先下载一个较小的缩略图
        Uri lowResUri = Uri.parse("https://ws1.sinaimg.cn/large/610dc034ly1fftusiwb8hj20u00zan1j.jpg");
        Uri highResUri = Uri.parse("http://img1.gtimg.com/19/1957/195726/19572624_980x1200_0.jpg");

        DraweeController controller00 = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
                .setImageRequest(ImageRequest.fromUri(highResUri))
                .setOldController(mDraweeView00.getController())
                .build();
        mDraweeView00.setController(controller00);

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable()) // 显示深蓝色的矩形进度条
                .build();
        mDraweeView00.setHierarchy(hierarchy);


        // 开启重试
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri("hhh")  // 假设错误地址
                .setTapToRetryEnabled(true)
                .setOldController(mDraweeView.getController()) // 在指定一个新的controller的时候，使用setOldController，这可节省不必要的内存分配。
                .build();
        mDraweeView.setController(controller);


        //你也许想在图片下载完成后执行一些动作，比如使某个别的 View 可见，或者显示一些文字。你也许还想在下载失败后做一些事，比如向用户显示一条失败信息。
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.e("Final image received! " + "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            //如果允许呈现渐进式JPEG，同时图片也是渐进式图片，onIntermediateImageSet会在每个扫描被解码后回调
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                FLog.e("onIntermediateImageSet", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };
        Uri uri1 = Uri.parse(BIG_PATH);
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri1)
                .setOldController(mDraweeView1.getController())
                .build();
        mDraweeView1.setController(controller1);


        // Gif自动播放
        DraweeController controller3 = Fresco.newDraweeControllerBuilder()
                .setUri(GIF_PATH)
                .setAutoPlayAnimations(true)
                .setOldController(mDraweeView3.getController())
                .build();
        mDraweeView3.setController(controller3);


        // 动态webp加载
        Uri uri4 = Uri.parse("res://com.example.cxh.imageloadersample/" + R.drawable.gif);

        DraweeController controller4 = Fresco.newDraweeControllerBuilder()
                .setUri(uri4)
                .setAutoPlayAnimations(true)
                .setOldController(mDraweeView4.getController())
                .build();
        mDraweeView4.setController(controller4);


        mDraweeView5.setImageURI(SMALL_PATH);
        mDraweeView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        KLog.e(hidden);
        // 控制动画，节省内存
        Animatable animatable = mDraweeView3.getController().getAnimatable();
        if (animatable != null)
            if (hidden) {
                animatable.stop();
            } else {
                animatable.start();
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
