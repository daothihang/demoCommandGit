package com.example.sharefoodmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.sharefoodmanagement.adapter.ImageAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.Image;
import com.example.sharefoodmanagement.model.Restaurant;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.example.sharefoodmanagement.view.CustomItalyTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ImageView imgLogo, imBack, imgDelete;
    private CustomItalyTextView tvName, tvOpen, tvAddress, tvPhone, tvWeb, tvProvince, tvStatus;
    private Toolbar toolbar;
    private Restaurant currentRestaurant;
    private ImageAdapter slidingAdapter;
    private List<Image> imageSlidings = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        findId();
        initViews();
        loadData();
        getImages();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarRestaurantInfo);
        imBack = findViewById(R.id.imgBack);
        imgLogo = findViewById(R.id.imgResInfo);
        tvName = findViewById(R.id.tvNameResInfo);
        tvOpen = findViewById(R.id.tvOpenTimeResInfo);
        tvAddress = findViewById(R.id.tvAddressResInfo);
        tvPhone = findViewById(R.id.tvPhoneResInfo);
        tvWeb = findViewById(R.id.tvWebsiteResInfo);
        tvProvince = findViewById(R.id.tvProvinceResInfo);
        viewPager = findViewById(R.id.viewpager_res);
        imgDelete = findViewById(R.id.imgDelete);
        tvStatus = findViewById(R.id.tvStatusResInfo);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        setSupportActionBar(toolbar);
        slidingAdapter = new ImageAdapter(this, imageSlidings);
        viewPager.setAdapter(slidingAdapter);

        imBack.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        currentRestaurant = (Restaurant) intent.getSerializableExtra(Key.RESTAURANT);
        String url = currentRestaurant.getLogo().contains("http") ? currentRestaurant.getLogo() : API.STORAGE + currentRestaurant.getLogo();
        Glide.with(this).load(url).into(imgLogo);
        tvName.setText(currentRestaurant.getName().trim());
        tvOpen.setText(currentRestaurant.getTime_open().trim() + " - " + currentRestaurant.getTime_close().trim());
        tvAddress.setText(currentRestaurant.getAddress().trim());
        tvPhone.setText(currentRestaurant.getPhone().trim());
        tvWeb.setText(currentRestaurant.getWebsite().trim());
        tvProvince.setText(currentRestaurant.getProvince().getDescription().trim());
        if (currentRestaurant.getLocked() == 0){
            tvStatus.setText(getString(R.string.active));
        }else {
            tvStatus.setText(getString(R.string.lock));
//            imgDelete.setVisibility(View.GONE);
        }
    }

    private void getImages() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_IMAGE + "?id_restaurant=" + currentRestaurant.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("images");
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Image image = (Image) gson.fromJson(jsonArray.getJSONObject(i).toString(), Image.class);
                            imageSlidings.add(image);
                        }
                        slidingAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgDelete:

                if (currentRestaurant.getLocked() == 0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Bạn muốn khóa nhà hàng này?")
                            .setCancelable(false)
                            .setPositiveButton("Khóa", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    lockCompany(currentRestaurant.getId());
                                    finish();
                                }
                            })
                            .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Bạn muốn mở khóa nhà hàng này?")
                            .setCancelable(false)
                            .setPositiveButton("Mở", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    unlockCompany(currentRestaurant.getId());
                                    finish();
                                }
                            })
                            .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
        }
    }

    private void lockCompany(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , API.BLOCK_RESTAURANT + "?id_restaurant=" + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString().contains("true")) {
                    Toast.makeText(RestaurantInfoActivity.this, getString(R.string.block_success), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void unlockCompany(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , API.UNBLOCK_RESTAURANT + "?id_restaurant=" + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString().contains("true")) {
                    Toast.makeText(RestaurantInfoActivity.this, getString(R.string.unblock_success), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
