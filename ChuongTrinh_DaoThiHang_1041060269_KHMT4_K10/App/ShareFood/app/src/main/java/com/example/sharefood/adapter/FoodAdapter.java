package com.example.sharefood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sharefood.R;
import com.example.sharefood.constant.API;
import com.example.sharefood.model.Price;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Price> arrItem;

    public FoodAdapter(Context context, int layout, List<Price> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        ImageView img;
        TextView tvName, tvPrice;
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
                holder.tvName = viewRow.findViewById(R.id.tvFoodName);
            }
            if (viewRow != null) {
                holder.tvPrice = viewRow.findViewById(R.id.tvFoodPrice);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgLogoFood);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Price item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvName.setText(item.getFood().getName());
        }
        if (holder != null) {
            holder.tvPrice.setText(item.getPrice() + " VND");
        }
        if (holder != null) {
            String url = item.getFood().getImage().contains("http")?item.getFood().getImage(): API.STORAGE + item.getFood().getImage();
            Glide.with(context).load(url).into(holder.img);
        }

//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RestaurantInfoActivity.class);
//                intent.putExtra(Key.RESTAURANT, item);
//                context.startActivity(intent);
//            }
//        });

        return viewRow;
    }


}