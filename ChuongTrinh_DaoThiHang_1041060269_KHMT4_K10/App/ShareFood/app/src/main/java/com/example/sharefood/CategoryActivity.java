package com.example.sharefood;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.adapter.CategoryAdapter;
import com.example.sharefood.constant.API;
import com.example.sharefood.model.Category;
import com.example.sharefood.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CategoryAdapter categoryAdapter;
    private RequestQueue requestQueue;
    private List<Category> categories;
    private ImageView imgDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findId();
        initViews();
    }

    private void findId() {
        viewPager = findViewById(R.id.viewpager);
        imgDemo = findViewById(R.id.imgDemo);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager(), categories);
        viewPager.setAdapter(categoryAdapter);
        getCategory();

        CountDownTimer countDownTimer = new CountDownTimer(2000,1000 ) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                imgDemo.setVisibility(View.GONE);
            }
        }.start();
    }

    private void getCategory() {
        categories.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_CATEGORY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray dataStr = response.getJSONObject("data").getJSONArray("categories");

                        if (dataStr.length() == 0) {
                            Toast.makeText(CategoryActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < dataStr.length(); i++) {
                                Category category = new Gson().fromJson(dataStr.getJSONObject(i).toString(), Category.class);
                                categories.add(category);
                            }

                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void switchActivity(int category){
        Intent intent = new Intent(this, ListFoodActivity.class);
        intent.putExtra("id_category", category);
        startActivity(intent);
    }
}
