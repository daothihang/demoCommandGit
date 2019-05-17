package com.example.sharefoodmanagement.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.fragment.CategoryManagerFragment;
import com.example.sharefoodmanagement.fragment.FoodManagerFragment;
import com.example.sharefoodmanagement.fragment.RestaurantInfoFragment;
import com.example.sharefoodmanagement.fragment.RestaurantManagerFragment;
import com.example.sharefoodmanagement.fragment.UserManagerFragment;

public class PagerManagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PagerManagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RestaurantInfoFragment();
        } else if (position == 1){
            return new FoodManagerFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.restaurant_desc);
            case 1:
                return mContext.getString(R.string.food_list);
            default:
                return null;
        }
    }

}