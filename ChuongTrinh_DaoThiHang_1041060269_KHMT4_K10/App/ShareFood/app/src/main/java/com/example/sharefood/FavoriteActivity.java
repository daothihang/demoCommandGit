package com.example.sharefood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.example.sharefood.adapter.FoodAdapter;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Favorite;
import com.example.sharefood.model.Price;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.example.sharefood.view.CustomListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Toolbar toolbar;
    private CustomListView lvFood;
    private List<Price> foods;
    private FoodAdapter adapter;
    private RequestQueue requestQueue;
    private Favorite favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        findId();
        initViews();
    }

    private void findId() {
        lvFood = findViewById(R.id.lvFood);
        toolbar = findViewById(R.id.toolbarFav);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        foods = new ArrayList<>();
        favorite = new Gson().fromJson(Preferences.getData(Key.FAV, FavoriteActivity.this), Favorite.class);
        foods.addAll(favorite.getPrices());
        adapter = new FoodAdapter(this, R.layout.item_price, foods);
        lvFood.setAdapter(adapter);


        lvFood.setOnItemClickListener(this);
        lvFood.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(FavoriteActivity.this, FoodDetailActivity.class);
        intent.putExtra(Key.FOOD, foods.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
        builder.setMessage("Bạn muốn xóa món ăn này khỏi danh sách yêu thích?")
                .setCancelable(false)
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        foods.remove(foods.get(position));
                        adapter.notifyDataSetChanged();
                        favorite.setPrices(foods);
                        Preferences.saveData(Key.FAV, new Gson().toJson(favorite), FavoriteActivity.this);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
}
