package com.example.sharefood.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sharefood.fragment.CategoryFragment;
import com.example.sharefood.model.Category;

import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Category> categories;

    public CategoryAdapter(Context context, FragmentManager fm, List<Category> categories) {
        super(fm);
        mContext = context;
        this.categories = categories;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", categories.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}