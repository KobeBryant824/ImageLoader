package com.example.cxh.imageloadersample.glide;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.cxh.imageloadersample.DensityUtils;
import com.example.cxh.imageloadersample.R;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.cxh.imageloadersample.MainActivity.BIG_PATH;
import static com.example.cxh.imageloadersample.MainActivity.GIF_PATH;
import static com.example.cxh.imageloadersample.MainActivity.SMALL_PATH;
import static com.example.cxh.imageloadersample.MainActivity.THUMBNAIL_PATH;

/**
 * Glide特点：使用简单，可配置度高，自适应程度高
 * 支持常见图片格式 Jpg png gif webp
 * 支持多种数据源  网络、本地、资源、Assets 等，可以显示本地视频
 * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
 * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
 * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity, 同时将Activity/Fragment作为with()参数的好处是：图片加载会和Activity/Fragment的生命周期保持一致
 * 你的请求需要在 activity 生命周期之外去做请用context.getApplicationContext()
 * crossFade(),3.6以上默认激活，dontAnimate()无淡入淡出效果
 */
/**
 * Desc:
 * Created by Hai (haigod7@gmail.com) on 2017/5/18 11:15.
 */
public class GlideFragment extends Fragment {
    private static int NOTIFICATION_ID = 1;
    @BindView(R.id.strategy_iv)
    ImageView mStrategyIv;
    @BindView(R.id.strategy_iv1)
    ImageView mStrategyIv1;
    @BindView(R.id.override_iv)
    ImageView mOverrideIv;
    @BindView(R.id.transform_iv)
    ImageView mTransformIv;
    @BindView(R.id.transform_iv1)
    ImageView mTransformIv1;
    @BindView(R.id.transform_iv2)
    ImageView mTransformIv2;
    @BindView(R.id.transform_iv3)
    ImageView mTransformIv3;
    @BindView(R.id.gif_iv)
    ImageView mGifIv;

    Unbinder unbinder;
    private View mView;
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_glide, container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //默认 fitCenter,剪裁，既缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。
        //centerCrop，剪裁，即缩放图像让它填充到 ImageView 界限内并且裁剪额外的部分。
        //两者共同点：该图像将会完全显示，但可能不会填满整个 ImageView,如果还是想完全充满控件还是得配合android:scaleType="centerCrop"

        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this).load(THUMBNAIL_PATH);

        //用了DiskCacheStrategy.ALL 策略，发现不同大小的imageview还是重新进行了网络请求 并没有从缓存取原图去调整，所以这是个鸡肋？详细看strategy.png
        Glide.with(this).load(BIG_PATH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder)
                .thumbnail(thumbnailRequest) //缩略图也可以是其他地址
                .priority(Priority.IMMEDIATE) //优先级并不是完全严格遵守的，小图还是会先加载
                .into(mStrategyIv);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(GlideFragment.this).load(BIG_PATH).centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .thumbnail(0.1f) //先显示缩略图再显示最终尺寸
                        .into(mStrategyIv1);
            }
        }, 10000);

        //如果控件宽高是包裹的，可以使用 overrid 调整宽高
        Glide.with(this).load(SMALL_PATH)
                .override(DensityUtils.dp2px(getContext(), 150), DensityUtils.dp2px(getContext(), 150))
                .placeholder(R.drawable.ic_placeholder)
                .into(mOverrideIv);

        //更多转换https://github.com/wasabeef/glide-transformations
        //转换为圆形图片
        BitmapImageViewTarget mBitmapImageViewTarget = new BitmapImageViewTarget(mTransformIv) {
            @Override
            protected void setResource(Bitmap resource) {
                KLog.e(resource.getByteCount());
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mTransformIv.setImageDrawable(circularBitmapDrawable);
            }
        };

        Glide.with(this)
                .load(SMALL_PATH)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(mBitmapImageViewTarget);

        Glide.with(this)
                .load(SMALL_PATH)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(new MySimpleTarget(DensityUtils.dp2px(getContext(), 150), DensityUtils.dp2px(getContext(), 150)));

        Glide.with(this)
                .load(SMALL_PATH)
                .asBitmap()
                .centerCrop()
                .transform(new CircleTransform(getContext()))
                .placeholder(R.drawable.ic_placeholder)
                .into(mTransformIv2);

        Glide.with(this)
                .load(SMALL_PATH)
                .asBitmap()
                .centerCrop()
                .transform(new RotateTransformation(getContext(),90f))
                .placeholder(R.drawable.ic_placeholder)
                .into(mTransformIv3);

        // GIF
        Glide.with(this)
                .load(GIF_PATH)
                .asGif()
                .error(R.drawable.ic_placeholder)
                .into(mGifIv);


        setNotificationTarget();

    }

    private void setNotificationTarget() {
        final RemoteViews rv = new RemoteViews(getContext().getPackageName(), R.layout.remoteview_notification);

        rv.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.ic_launcher);
        rv.setTextViewText(R.id.remoteview_notification_short_message, "Short Message");
        rv.setTextViewText(R.id.remoteview_notification_headline, "Headline");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Content Text")
                        .setContentTitle("Content Title")
                        .setContent(rv)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        final Notification notification = mBuilder.build();

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = rv;
        }

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);


        NotificationTarget notificationTarget = new NotificationTarget(
                getContext(),
                rv,
                R.id.remoteview_notification_icon,
                notification,
                NOTIFICATION_ID);

        Glide
                .with(getContext().getApplicationContext()) // safer!
                .load(THUMBNAIL_PATH)
                .asBitmap()
                .into(notificationTarget);
    }

    private class MySimpleTarget extends SimpleTarget<Bitmap> {

        public MySimpleTarget(int width, int height) {
            super(width, height);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            KLog.e(resource.getByteCount());
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            mTransformIv1.setImageDrawable(circularBitmapDrawable);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
