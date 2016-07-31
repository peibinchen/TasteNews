package com.example.asus.tastenews.main.widget;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.about.widget.AboutFragment;
import com.example.asus.tastenews.bluetoothshare.widget.ShareNewsFragment;
import com.example.asus.tastenews.floatingwindow.widget.FloatingFragment;
import com.example.asus.tastenews.guide.widget.GuideFragment;
import com.example.asus.tastenews.images.widget.ImageFragment;
import com.example.asus.tastenews.main.presenter.MainPresenter;
import com.example.asus.tastenews.main.presenter.MainPresenterImpl;
import com.example.asus.tastenews.main.view.MainView;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.weather.widget.WeatherFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MainView {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private MainPresenter mMainPresenter;
    private CircleImageView mHeadIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);

        setupDrawerLayout(mNavigationView);

        mHeadIV = (CircleImageView)mNavigationView.getHeaderView(0).findViewById(R.id.iv_head);
        //mHeadIV = (CircleImageView)mDrawerLayout.findViewById(R.id.iv_head);

        mMainPresenter = new MainPresenterImpl(this);

        switch2News();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        /**
         * 重写这个函数的目的是因为fragment在activity被回收后不会被回收，当activity再次创建时，
         * 因为activity之前已经保存fragment在Bundle中了，所以会恢复fragment，但是activity重启
         * 也会再次生成fragment，两层fragment重叠，恢复的fragment使用getActivity()为null，于是
         * 报错。
         * 传输门：http://blog.csdn.net/goodlixueyong/article/details/48715661
         */

        //super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_setting){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerLayout(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        mMainPresenter.switchNavigation(item.getItemId());
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public void switch2Guide(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new GuideFragment()).commit();
    }

    @Override
    public void switch2News(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new NewsFragment()).commit();
        mToolbar.setTitle(R.string.navigation_news);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_content,new ShareNewsFragment()).commit();
//        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2Images(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new ImageFragment()).commit();
        mToolbar.setTitle(getString(R.string.navigation_images));
    }

    @Override
    public void switch2Weather(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new WeatherFragment()).commit();
        mToolbar.setTitle(getString(R.string.navigation_weather));
    }

    @Override
    public void switch2About(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new AboutFragment()).commit();
        mToolbar.setTitle(getString(R.string.navigation_about));
    }

    @Override
    public void switch2FloatingWindow() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content,new FloatingFragment()).commit();
        mToolbar.setTitle(getString(R.string.floating_switch));
    }

}
