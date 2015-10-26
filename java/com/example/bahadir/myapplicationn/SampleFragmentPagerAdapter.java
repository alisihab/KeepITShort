package com.example.bahadir.myapplicationn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    final int PAGE_COUNT = 5;
    private int tabIcons[] = {R.mipmap.iconselect, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher};

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return PageFragment.newInstance(position + 1);}
        else if(position==1){
            return PageFragment1.newInstance(position + 1);}
        else if(position==2){
            return PageFragment2.newInstance(position + 1);}
        else if(position==3){
            return PageFragment3.newInstance(position + 1);}
        else if(position==4){
            return PageFragment4.newInstance(position + 1);}
        else return PageFragment4.newInstance(position + 1);
    }


    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}