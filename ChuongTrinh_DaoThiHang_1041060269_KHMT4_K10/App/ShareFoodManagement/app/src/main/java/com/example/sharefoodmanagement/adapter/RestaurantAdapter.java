package com.example.sharefoodmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sharefoodmanagement.MainAdminActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.RestaurantInfoActivity;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private int layout;
    private List<Restaurant> arrItem;
    private List<Restaurant> orig;

    public RestaurantAdapter(Context context, int layout, List<Restaurant> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Restaurant> results = new ArrayList<Restaurant>();
                if (orig == null)
                    orig = arrItem;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Restaurant g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                arrItem = (ArrayList<Restaurant>) results.values;
                notifyDataSetChanged();
            }
        };
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
                holder.tvTitle = viewRow.findViewById(R.id.tvNameItemGVCompany);
            }
            if (viewRow != null) {
                holder.img = viewRow.findViewById(R.id.imgLogoItemGvCompany);
            }
            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Restaurant item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }
        if (holder != null) {
            holder.tvTitle.setText(item.getName());
        }
        if (holder != null) {
            String url = item.getLogo().contains("http")?item.getLogo(): API.STORAGE + item.getLogo();
            Glide.with(context).load(url).into(holder.img);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantInfoActivity.class);
                intent.putExtra(Key.RESTAURANT, item);
                context.startActivity(intent);
            }
        });

        return viewRow;
    }


}