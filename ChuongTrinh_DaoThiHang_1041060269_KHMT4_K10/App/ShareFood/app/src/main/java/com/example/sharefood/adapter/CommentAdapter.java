package com.example.sharefood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sharefood.R;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Cart;
import com.example.sharefood.model.FoodComment;
import com.example.sharefood.model.ShoppingCart;
import com.example.sharefood.util.Preferences;
import com.google.gson.Gson;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<FoodComment> arrItem;

    public CommentAdapter(Context context, int layout, List<FoodComment> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvName, tvComment, tvTime;
        ImageView imgLogo;
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
                holder.tvName = viewRow.findViewById(R.id.tvNameComment);
            }
            if (viewRow != null) {
                holder.tvComment = viewRow.findViewById(R.id.tvComment);
            }
            if (viewRow != null) {
                holder.imgLogo = viewRow.findViewById(R.id.imgAvatarComment);
            }
            if (viewRow != null) {
                holder.tvTime = viewRow.findViewById(R.id.tvTimeComment);
            }

            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final FoodComment item = arrItem.get(i);
        final ViewHolder holder = (ViewHolder) viewRow.getTag();
        holder.tvName.setText(item.getUser().getName());
        holder.tvComment.setText(item.getComment());

        Glide.with(context).load(item.getUser().getAvatar()).into(holder.imgLogo);

        holder.tvTime.setText(item.getCreated_at());

        return viewRow;
    }

}