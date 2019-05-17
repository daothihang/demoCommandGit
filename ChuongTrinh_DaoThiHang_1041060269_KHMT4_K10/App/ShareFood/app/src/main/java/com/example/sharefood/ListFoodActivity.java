package com.example.sharefood;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.adapter.FoodAdapter;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Price;
import com.example.sharefood.model.Province;
import com.example.sharefood.util.VolleySingleton;
import com.example.sharefood.view.CustomItalyTextView;
import com.example.sharefood.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFoodActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private CustomListView lvFood;
    private List<Price> foods;
    private FoodAdapter adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        findId();
        initViews();
    }

    private void findId() {
        lvFood = findViewById(R.id.lvFood);
        toolbar = findViewById(R.id.toolbarListFood);
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
        adapter = new FoodAdapter(this, R.layout.item_price, foods);
        lvFood.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id_category", -1);

        getPrice(id);

        lvFood.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPrice(int id_category) {
        foods.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.SEARCH_PRICE_WITH_CATEGORY + "?id_category=" + id_category, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray data = response.getJSONObject("data").getJSONArray("prices");
                        for (int i=0; i<data.length(); i++){
                            Price price = new Gson().fromJson(data.getJSONObject(i).toString(), Price.class);
                            foods.add(price);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListFoodActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ListFoodActivity.this, FoodDetailActivity.class);
        intent.putExtra(Key.FOOD, foods.get(position));
        startActivity(intent);
    }
}
