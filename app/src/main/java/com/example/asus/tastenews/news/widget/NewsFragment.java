package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.tastenews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻界面：包括四个部分，用TabLayout和ViewPager管理
 */
public class NewsFragment extends Fragment {
    public final static int NEWS_TYPE_TOP = 0;
    public final static int NEWS_TYPE_NBA = 1;
    public final static int NEWS_TYPE_CARS = 2;
    public final static int NEWS_TYPE_JOKES = 3;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_news,null);

        mTabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.top));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.nba));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.jokes));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.cars));
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager绑定

        return view;
    }

    /**
     * 添加四个fragment，fragment用NewsListFragment来创建，NewsListFragment
     * 中创建时给每个fragment一个Argument参数，实际上就是type，用来表示不同的fragment。
     * NewsListFragment用来控制presenter层，从而实现数据的显示和传输。
     * @param mViewPager
     */
    private void setupViewPager(ViewPager mViewPager){

        // getChildFragmentManager()用于对Fragment中嵌套的Fragment进行管理
        MyPageAdapter adapter = new MyPageAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_TOP),getString(R.string.top));
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_NBA),getString(R.string.nba));
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_JOKES),getString(R.string.jokes));
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_CARS),getString(R.string.cars));

        mViewPager.setAdapter(adapter);
    }

    /**
     * ViewPager的适配器，用来对Fragment管理
     */
    public static class MyPageAdapter extends FragmentPagerAdapter{
        private final List<Fragment>mFragments = new ArrayList<>();
        private final List<String>mFragmentTitles = new ArrayList<>();

        public MyPageAdapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment,String title){
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position){
            return mFragments.get(position);
        }

        @Override
        public int getCount(){
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitles.get(position);
        }
    }
}

