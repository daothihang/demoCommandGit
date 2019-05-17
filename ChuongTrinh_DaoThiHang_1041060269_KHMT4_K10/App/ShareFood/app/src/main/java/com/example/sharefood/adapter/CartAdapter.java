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
import com.example.sharefood.model.ShoppingCart;
import com.example.sharefood.util.Preferences;
import com.google.gson.Gson;

import java.util.List;

public class CartAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private List<Cart> arrItem;
    private TextView tvTotal;

    public CartAdapter(Context context, int layout, List<Cart> arrItem, TextView tvTotal) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
        this.tvTotal = tvTotal;
    }

    private class ViewHolder{
        TextView tvName, tvPrice;
        ImageView imgLogo, imRemove, imAdd;
        EditText edtCount;
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
                holder.tvName = viewRow.findViewById(R.id.tvCartName);
            }
            if (viewRow != null) {
                holder.tvPrice = viewRow.findViewById(R.id.tvPriceCart);
            }
            if (viewRow != null) {
                holder.imgLogo = viewRow.findViewById(R.id.imgLogoCart);
            }
            if (viewRow != null) {
                holder.imAdd = viewRow.findViewById(R.id.imgAddCount);
            }
            if (viewRow != null) {
                holder.imRemove = viewRow.findViewById(R.id.imgRemoveCount);
            }
            if (viewRow != null) {
                holder.edtCount = viewRow.findViewById(R.id.edtShowCount);
            }

            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final Cart cart = arrItem.get(i);
        final ViewHolder holder = (ViewHolder) viewRow.getTag();
        holder.tvName.setText(cart.getPrice().getFood().getName());
        calculatePrice(tvTotal);
        holder.tvPrice.setText(String.valueOf((cart.getPrice().getPrice() * cart.getQuantum()))+ " VND");

        Glide.with(context).load(cart.getPrice().getFood().getImage()).into(holder.imgLogo);

        holder.edtCount.setText(String.valueOf(cart.getQuantum()));

        holder.imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextCount = Integer.parseInt(holder.edtCount.getText().toString()) + 1;
                if (nextCount != 100){
                    holder.edtCount.setText(String.valueOf(nextCount));
                    cart.setQuantum(nextCount);
                    holder.tvPrice.setText(String.valueOf(cart.getPrice().getPrice() * nextCount) + " VND");
                    cart.setAmount(cart.getPrice().getPrice() * nextCount);
                    Preferences.saveData(Key.SHOPPING_CART, new Gson().toJson(new ShoppingCart(arrItem)), context);
                    calculatePrice(tvTotal);
                }else {
                    Toast.makeText(context, context.getString(R.string.max_count), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int preCount = Integer.parseInt(holder.edtCount.getText().toString()) - 1;
                if (preCount == 0){
                    arrItem.remove(cart);
                    notifyDataSetChanged();
                    Preferences.saveData(Key.SHOPPING_CART, new Gson().toJson(new ShoppingCart(arrItem)), context);
                    calculatePrice(tvTotal);
                }else {
                    holder.edtCount.setText(String.valueOf(preCount));
                    cart.setQuantum(preCount);
                    holder.tvPrice.setText(String.valueOf(cart.getPrice().getPrice() * preCount) + " VND");
                    cart.setAmount(cart.getPrice().getPrice() * preCount);
                    Preferences.saveData(Key.SHOPPING_CART, new Gson().toJson(new ShoppingCart(arrItem)), context);
                    calculatePrice(tvTotal);
                }
            }
        });

        return viewRow;
    }

    private void calculatePrice(TextView tv){
        int total = 0;
        for (int i=0; i<arrItem.size(); i++){
            total += arrItem.get(i).getAmount();
        }
        tv.setText(total + " VND");
    }


}