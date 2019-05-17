package com.example.sharefood;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.adapter.FoodAdapter;
import com.example.sharefood.adapter.PriceAdapter;
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

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private Spinner spinnerProvince;
    private CustomListView lvFood;
    private List<Price> foods;
    private FoodAdapter adapter;
    private RequestQueue requestQueue;
    private List<Province> provinces = new ArrayList<>();
    private CustomItalyTextView tvProvinceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarSearch);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        lvFood = findViewById(R.id.lvFoodSearch);
        tvProvinceInfo =findViewById(R.id.tvProvinceInfo);
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

        lvFood.setOnItemClickListener(this);
        getAllProvince();

        spinnerProvince.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllProvince() {
        provinces.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_PROVINCE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray data = response.getJSONObject("data").getJSONArray("provinces");
                        for (int i=0; i<data.length(); i++){
                            Province province = new Gson().fromJson(data.getJSONObject(i).toString(), Province.class);
                            provinces.add(province);
                        }

                        String[] provinceStr = new String[65];
                        provinceStr[0] = getString(R.string.select_province);
                        for (int i=0; i< provinces.size(); i++){
                            provinceStr[i+1] = provinces.get(i).getName();
                        }
                        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, provinceStr);
                        spinnerProvince.setAdapter(itemAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvProvinceInfo.setText("");
        if (position != 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvProvinceInfo.setText(Html.fromHtml(provinces.get(position - 1).getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvProvinceInfo.setText(Html.fromHtml(provinces.get(position - 1).getDescription()));
            }
            getPrice(provinces.get(position - 1).getId_province());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getPrice(int id_province) {
        foods.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.SEARCH_PRICE + "?id_province=" + id_province, null, new Response.Listener<JSONObject>() {
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
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchActivity.this, FoodDetailActivity.class);
        intent.putExtra(Key.FOOD, foods.get(position));
        startActivity(intent);
    }
}
