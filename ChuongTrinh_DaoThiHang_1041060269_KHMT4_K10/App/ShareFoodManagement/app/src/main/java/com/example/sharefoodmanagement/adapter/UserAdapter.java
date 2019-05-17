package com.example.sharefoodmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.RestaurantInfoActivity;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.Restaurant;
import com.example.sharefoodmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> arrItem;

    public UserAdapter(Context context, int layout, List<User> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }


    private class ViewHolder{
        ImageView img;
        TextView tvTitle;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewRow = view;
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                viewRow = inflater.inflate(layout,viewGroup,false);
            }

            ViewHolder holder = new ViewHolder();
            if (viewRow != null) {
                holder.tvTitle = viewRow.findViewById(R.id.tvNameUser);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgAvatrUser);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final User item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getName());
        }
        if (holder != null) {
            String url = item.getAvatar().contains("http")?item.getAvatar(): API.STORAGE + item.getAvatar();
            Glide.with(context).load(url).into(holder.img);
        }

        return viewRow;
    }


}