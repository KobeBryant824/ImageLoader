package com.example.cxh.imageloadersample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.cxh.imageloadersample.fresco.FrescoFragment;
import com.example.cxh.imageloadersample.glide.GlideFragment;

public class MainActivity extends AppCompatActivity {
    public static final String SMALL_PATH = "http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg";
    public static final String BIG_PATH = "http://wallpaper.macappstore.net/d/file/wallpaper/2017/05-14/1960-1225-22433562a918104eb2dbbfb782a491a8.jpg";
    public static final String GIF_PATH = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3614697974,227598936&fm=23&gp=0.jpg";
    public static final String THUMBNAIL_PATH = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495434060079&di=68839f3595a287178c64339b57e17ccb&imgtype=0&src=http%3A%2F%2Ft3.qlogo.cn%2Fmbloghead%2F28949cfa1b15223f9840%2F180";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_glide:
                    switchContent(mGlideFragment, mFrescoFragment);
                    return true;
                case R.id.navigation_fresco:
                    switchContent(mFrescoFragment, mGlideFragment);
                return true;
            }
            return false;
        }

    };
    private GlideFragment mGlideFragment;
    private FrescoFragment mFrescoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGlideFragment = new GlideFragment();
        mFrescoFragment = new FrescoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mGlideFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void switchContent(Fragment a, Fragment b){
        getSupportFragmentManager().beginTransaction().show(a).hide(b).commit();
    }

}
