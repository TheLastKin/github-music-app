package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPager_Adapter extends PagerAdapter {
    private ArrayList<View> dslayout;
    public ViewPager_Adapter(ArrayList<View> dslayout) {
        this.dslayout = dslayout;
    }

    @Override
    public int getCount() {
        return dslayout.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(dslayout.get(position));
        return dslayout.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(dslayout.get(position));
    }
}
