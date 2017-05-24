package com.example.cxh.imageloadersample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.cxh.imageloadersample.fresco.FrescoFragment;
import com.example.cxh.imageloadersample.glide.GlideFragment;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends AppCompatActivity {
    public static final String SMALL_PATH = "http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg";
    public static final String BIG_PATH = "http://wallpaper.macappstore.net/d/file/wallpaper/2017/05-14/1960-1225-22433562a918104eb2dbbfb782a491a8.jpg";
    public static final String GIF_PATH = "http://res.cloudinary.com/demo/image/upload/bored_animation.gif";
    public static final String THUMBNAIL_PATH = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495434060079&di=68839f3595a287178c64339b57e17ccb&imgtype=0&src=http%3A%2F%2Ft3.qlogo.cn%2Fmbloghead%2F28949cfa1b15223f9840%2F180";

    private GlideFragment mGlideFragment;
    private FrescoFragment mFrescoFragment;
    private View mGlideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrescoFragment = new FrescoFragment();
        mGlideFragment = new GlideFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mGlideFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.content1, mFrescoFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mGlideView = findViewById(R.id.content);
    }

    public void switchContent(Fragment a, Fragment b) {
        getSupportFragmentManager().beginTransaction().show(a).hide(b).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_fresco:
                    switchContent(mFrescoFragment, mGlideFragment);
                    return true;
                case R.id.navigation_glide:
                    if (mGlideView.getVisibility() == View.INVISIBLE) mGlideView.setVisibility(View.VISIBLE);
                    switchContent(mGlideFragment, mFrescoFragment);
                    return true;
            }
            return false;
        }

    };

}
