package com.example.sharefoodmanagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefoodmanagement.MainManagerActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.adapter.FoodAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.Category;
import com.example.sharefoodmanagement.model.Food;
import com.example.sharefoodmanagement.model.Manager;
import com.example.sharefoodmanagement.model.Price;
import com.example.sharefoodmanagement.model.Restaurant;
import com.example.sharefoodmanagement.model.User;
import com.example.sharefoodmanagement.util.Preferences;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.example.sharefoodmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodManagerFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private CustomListView lvFood;
    private FloatingActionButton btnAdd;
    private RequestQueue requestQueue;
    private MainManagerActivity mainManagerActivity;
    private FoodAdapter adapter;
    private List<Price> foods;
    private Manager currentManager;
    private User currentUser;
    private Dialog dialog;
    private List<Category> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        lvFood = view.findViewById(R.id.lvFood);
        btnAdd = view.findViewById(R.id.btnAddFood);
    }

    private void initViews() {
        mainManagerActivity = (MainManagerActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        foods = new ArrayList<>();
        adapter = new FoodAdapter(mainManagerActivity, R.layout.item_price, foods);
        lvFood.setAdapter(adapter);

//        currentManager = new Gson().fromJson(Preferences.getData(Key.MANAGER, mainManagerActivity), Manager.class);
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainManagerActivity), User.class);

        getManager(currentUser.getId());
        lvFood.setOnItemLongClickListener(this);
        lvFood.setOnItemClickListener(this);

        btnAdd.setOnClickListener(this);
        getCategory();

    }

    private void getFood() {
        foods.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_PRICE + "?id_restaurant=" + currentManager.getRestaurant().getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray data = response.getJSONObject("data").getJSONArray("prices");
                        for (int i = 0; i < data.length(); i++) {
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
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getManager(int idUser) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_MANAGER + "?id_user=" + idUser, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject dataStr = response.getJSONObject("data").getJSONObject("manager");

                        currentManager = new Gson().fromJson(dataStr.toString(), Manager.class);
                        Preferences.saveData(Key.MANAGER, new Gson().toJson(currentManager), mainManagerActivity);

                        getFood();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(mainManagerActivity);
//        builder.setMessage("Bạn muốn xóa món ăn này?")
//                .setCancelable(false)
//                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        Price  price = foods.get(position);
//                        deletePrice(price.getId_price());
//                        dialog.cancel();
//                    }
//                })
//                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();


        return true;
    }

    private void deletePrice(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, API.PRICE_WITH_PAGE + "?id_price=" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainManagerActivity, "Xóa thành công", Toast.LENGTH_SHORT).show();

                        getFood();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//        initDialogUpdate(foods.get(position));
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.updateCatgory:
                        initDialogUpdate(foods.get(position));
                        break;
                    case R.id.deleteCategory:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mainManagerActivity);
                        builder.setMessage("Bạn muốn xóa món ăn này?")
                                .setCancelable(false)
                                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        Price price = foods.get(position);
                                        deletePrice(price.getId_price());
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
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    @SuppressLint("NewApi")
    private void initDialogUpdate(final Price price) {
        dialog = new Dialog(mainManagerActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_info_food);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        final EditText edtName = dialog.findViewById(R.id.edtNameFood);
        final EditText edtDesc = dialog.findViewById(R.id.edtDescFood);
        final EditText edtImage = dialog.findViewById(R.id.edtImageFood);
        final EditText edtRecipe = dialog.findViewById(R.id.edtRecipeFood);
        final EditText edtPrice = dialog.findViewById(R.id.edtPriceFood);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateFood);

        edtName.setText(price.getFood().getName());
        edtDesc.setText(price.getFood().getDescription());
        edtImage.setText(price.getFood().getImage());
        edtRecipe.setText(price.getFood().getRecipe());
        edtPrice.setText(price.getPrice() + "");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String desc = edtDesc.getText().toString();
                String image = edtImage.getText().toString();
                String recipe = edtRecipe.getText().toString();
                String pr = edtPrice.getText().toString();

                if (name.isEmpty() || desc.isEmpty() || image.isEmpty() || recipe.isEmpty() || pr.isEmpty()) {
                    Toast.makeText(mainManagerActivity, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }


                Food food = price.getFood();
                food.setName(name);
                food.setDescription(desc);
                food.setImage(image);
                food.setRecipe(recipe);
                price.setFood(food);
                price.setPrice(Integer.parseInt(pr));

                updateFood(food, price);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @SuppressLint("NewApi")
    private void initDialogPost() {
        dialog = new Dialog(mainManagerActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_add_food);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        final EditText edtName = dialog.findViewById(R.id.edtNameFood);
        final EditText edtDesc = dialog.findViewById(R.id.edtDescFood);
        final EditText edtImage = dialog.findViewById(R.id.edtImageFood);
        final EditText edtRecipe = dialog.findViewById(R.id.edtRecipeFood);
        final Spinner spinner = dialog.findViewById(R.id.spinnerCategory);
        final EditText edtPrice = dialog.findViewById(R.id.edtPriceFood);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateFood);


        String[] cate = new String[categories.size() + 1];
        cate[0] = "Chọn danh mục món ăn";
        for (int i = 0; i < categories.size(); i++) {
            cate[i + 1] = categories.get(i).getName();
        }

        final ArrayAdapter<String> item = new ArrayAdapter<>(mainManagerActivity, android.R.layout.simple_list_item_1, cate);
        spinner.setAdapter(item);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String desc = edtDesc.getText().toString();
                String image = edtImage.getText().toString();
                String recipe = edtRecipe.getText().toString();
                String price = edtPrice.getText().toString();

                if (name.isEmpty() || desc.isEmpty() || image.isEmpty() || recipe.isEmpty() || price.isEmpty()) {
                    Toast.makeText(mainManagerActivity, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                int select = spinner.getSelectedItemPosition();

                if (select == 0) {
                    Toast.makeText(mainManagerActivity, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Category category = categories.get(select - 1);

                Food food = new Food(name, desc, image, recipe, category);

                postRequest(food, Integer.parseInt(price), currentManager.getRestaurant());
                dialog.dismiss();
            }
        });


        dialog.show();
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
                            Toast.makeText(mainManagerActivity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < dataStr.length(); i++) {
                                Category category = new Gson().fromJson(dataStr.getJSONObject(i).toString(), Category.class);
                                categories.add(category);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void putFood(Price price) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(price));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.PRICE_WITH_PAGE, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainManagerActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        getFood();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateFood(Food food, final Price price) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(food));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.POST_FOOD, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        putFood(price);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postRequest(final Food food, final int price, final Restaurant restaurant) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(food));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.POST_FOOD, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Price price1 = new Price(price, new Gson().fromJson(response.getJSONObject("data").getJSONObject("food").toString(), Food.class), restaurant);
                        postPrice(price1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postPrice(Price price) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(price));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.PRICE_WITH_PAGE, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainManagerActivity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        getFood();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        initDialogPost();
    }
}
