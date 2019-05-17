package com.example.sharefoodmanagement.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.fragment.CategoryManagerFragment;
import com.example.sharefoodmanagement.fragment.RestaurantManagerFragment;
import com.example.sharefoodmanagement.fragment.UserManagerFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RestaurantManagerFragment();
        } else if (position == 1){
            return new UserManagerFragment();
        } else if (position == 2){
            return new CategoryManagerFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.restaurant_manager);
            case 1:
                return mContext.getString(R.string.user_manager);
            case 2:
                return mContext.getString(R.string.category_manager);
            default:
                return null;
        }
    }

}