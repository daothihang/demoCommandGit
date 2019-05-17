package com.example.sharefood;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.adapter.PriceAdapter;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Price;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.ProcessDialog;
import com.example.sharefood.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, PriceAdapter.OnClickItemListener {

    private RecyclerView recyclerViewFood;
    private PriceAdapter foodAdapter;
    private List<Price> foods = new ArrayList<>();
    private RequestQueue requestQueue;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int allFoods = 0;
    private int currentPage = 1;
    private Toolbar toolbar;
    private ImageView imMenu;
    private Dialog dialogMenu;
    private Dialog dialogCategory;
    private ProcessDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarMain);
        recyclerViewFood = findViewById(R.id.recyclerFood);
        shimmerFrameLayout = findViewById(R.id.shimmerFood);
        imMenu = findViewById(R.id.imMenu);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new PriceAdapter(recyclerViewFood, this, foods);
        recyclerViewFood.setAdapter(foodAdapter);
        foodAdapter.setOnItemClickListener(this);
        shimmerFrameLayout.startShimmer();
        processDialog = new ProcessDialog(this);
        countAllFood();

        loadMoreData(currentPage);

        //Set Load more event
        foodAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (foods.size() <= allFoods) // Change max size
                {
                    foods.add(null);
                    foodAdapter.notifyItemInserted(foods.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            foods.remove(foods.size() - 1);
                            foodAdapter.notifyItemRemoved(foods.size());
                            currentPage++;
                            loadMoreData(currentPage);

                        }
                    }, 3000); // Time to load
                } else {
                    Toast.makeText(Main2Activity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imMenu.setOnClickListener(this);
    }

    private void loadMoreData(final int page) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.PRICE_WITH_PAGE + "?page=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("prices");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Price price = (Price) gson.fromJson(jsonArray.getJSONObject(i).toString(), Price.class);
                            foods.add(price);
                        }
                        if (shimmerFrameLayout.isShimmerStarted()) {
                            CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                }
                            }.start();

                        }
                        foodAdapter.notifyDataSetChanged();
                        foodAdapter.setLoaded();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void countAllFood() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.COUNT_PRICE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject data = response.getJSONObject("data");
                        allFoods = data.getInt("count");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imMenu:
                initDialogMenu();
                break;
        }
    }

    private void initDialogComfirmPassword() {
        Dialog dialogComfirm = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogComfirm.setContentView(R.layout.dialog_confirm_password);
        Objects.requireNonNull(dialogComfirm.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogComfirm.setCancelable(true);
        EditText editComfirmPassword = dialogComfirm.findViewById(R.id.edtPasswordComfirm);
        Button btnComfirm = dialogComfirm.findViewById(R.id.btnComfirm);
        Button btnCancle = dialogComfirm.findViewById(R.id.btnCancle);
        final String userStr = Preferences.getData(Key.USER, Main2Activity.this);

        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userStr.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(userStr);
                        String password = jsonObject.getString("password");
                        if (editComfirmPassword.getText().toString().equals("" + password)) {
                            Intent intent = new Intent(Main2Activity.this,EditInfoActivity.class);
                            startActivity(intent);
                            dialogComfirm.dismiss();
                            dialogMenu.dismiss();
                        } else {
                            Toast.makeText(Main2Activity.this, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComfirm.dismiss();
            }
        });
        dialogComfirm.show();
    }

    @SuppressLint("NewApi")
    private void initDialogMenu() {
        dialogMenu = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogMenu.setContentView(R.layout.dialog_menu);
        Objects.requireNonNull(dialogMenu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMenu.setCancelable(true);

        Button btnSearch = dialogMenu.findViewById(R.id.btnSearch);
        Button btnCategory = dialogMenu.findViewById(R.id.btnCategory);
        Button btnEditInfo = dialogMenu.findViewById(R.id.btnEditInfo);
        Button btnRegister = dialogMenu.findViewById(R.id.btnResgisterRestaurant);
        Button btnLogin = dialogMenu.findViewById(R.id.btnLogin);
        Button btnFav = dialogMenu.findViewById(R.id.btnFavRestaurant);

        final String userStr = Preferences.getData(Key.USER, Main2Activity.this);
        if (!userStr.equals("")) {
            btnLogin.setText(getString(R.string.logout));
            btnEditInfo.setVisibility(View.VISIBLE);
        } else {
            btnEditInfo.setVisibility(View.INVISIBLE);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, SearchActivity.class));
                dialogMenu.dismiss();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userStr.equals("")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setMessage("Bạn muốn đăng xuất?")
                            .setCancelable(false)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    Preferences.saveData(Key.USER, "", Main2Activity.this);
                                    dialogMenu.dismiss();
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
                } else {
                    startActivity(new Intent(Main2Activity.this, LoginRegisterActivity.class));

                    dialogMenu.dismiss();
                }
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, CategoryActivity.class));
                dialogMenu.dismiss();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Preferences.getData(Key.USER, Main2Activity.this);
                if (user.equals("")) {
                    Toast.makeText(Main2Activity.this, getString(R.string.login_please), Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(Main2Activity.this, RegisterRestaurantActivity.class));
                dialogMenu.dismiss();
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, FavoriteActivity.class));
                dialogMenu.dismiss();
            }
        });
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //về nhà làm tiếp
                initDialogComfirmPassword();
            }
        });

        dialogMenu.show();
    }


//    CategoryAdapter categoryAdapter;
//    List<Category> categories = new ArrayList<>();
//    @SuppressLint("NewApi")
//    private void initDialogCategory() {
//        dialogCategory = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
//        dialogCategory.setContentView(R.layout.dialog_category);
//        Objects.requireNonNull(dialogMenu.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogCategory.setCancelable(true);
//
//        ViewPager viewPager = dialogCategory.findViewById(R.id.viewpager);
//
//        categoryAdapter = new CategoryAdapter(getApplicationContext(), getSupportFragmentManager(), categories);
//        viewPager.setAdapter(categoryAdapter);
//        getCategory();
//
//
//        dialogCategory.show();
//    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Main2Activity.this, FoodDetailActivity.class);
        intent.putExtra(Key.FOOD, foods.get(position));
        startActivity(intent);
    }


    @Override
    public void onButtonClick(View view, int position) {

    }
}