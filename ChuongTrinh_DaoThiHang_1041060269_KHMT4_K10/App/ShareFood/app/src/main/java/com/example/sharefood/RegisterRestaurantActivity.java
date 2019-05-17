package com.example.sharefood;

import android.app.TimePickerDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.FoodComment;
import com.example.sharefood.model.Image;
import com.example.sharefood.model.Manager;
import com.example.sharefood.model.Province;
import com.example.sharefood.model.Restaurant;
import com.example.sharefood.model.User;
import com.example.sharefood.util.DateTime;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RegisterRestaurantActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private EditText edtName, edtOpen, edtClose, edtAdress, edtPhone, edtWeb, edtLogo, edtThumbnail;
    private Button btnRegister;
    private RequestQueue requestQueue;
    private Spinner spinnerProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarRegisterRestaurant);
        edtName = findViewById(R.id.edtNameRes);
        edtOpen = findViewById(R.id.edtOpenTimeRes);
        edtClose = findViewById(R.id.edtCloseTimeRes);
        edtAdress = findViewById(R.id.edtAddressRes);
        edtPhone = findViewById(R.id.edtPhoneRes);
        edtWeb = findViewById(R.id.edtWebsiteRes);
        edtLogo = findViewById(R.id.edtLogoRes);
        btnRegister = findViewById(R.id.btnRegisterRes);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        edtThumbnail = findViewById(R.id.edtThumbnailRes);
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

        spinnerProvince.setOnItemSelectedListener(this);

        getAllProvince();

        btnRegister.setOnClickListener(this);
        edtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RegisterRestaurantActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String h = String.valueOf(selectedHour);
                        String m = String.valueOf(selectedMinute);
                        if (selectedHour < 10){
                            h = "0" + h;
                        }
                        if (selectedMinute < 10){
                            m = "0" + m;
                        }
                        edtOpen.setText( h + ":" + m);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Chọn thời gian");
                mTimePicker.show();
            }
        });

        edtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RegisterRestaurantActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String h = String.valueOf(selectedHour);
                        String m = String.valueOf(selectedMinute);
                        if (selectedHour < 10){
                            h = "0" + h;
                        }
                        if (selectedMinute < 10){
                            m = "0" + m;
                        }
                        edtClose.setText( h + ":" + m);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Chọn thời gian");
                mTimePicker.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String thumbnail = "";

    @Override
    public void onClick(View v) {
        String name = edtName.getText().toString();
        String open = edtOpen.getText().toString();
        String close = edtClose.getText().toString();
        String address = edtAdress.getText().toString();
        String phone = edtPhone.getText().toString();
        String web = edtWeb.getText().toString();
        String logo = edtLogo.getText().toString();
        String thumbnail = edtThumbnail.getText().toString();

        if (name.isEmpty() || open.isEmpty() || close.isEmpty() || address.isEmpty() || phone.isEmpty() || web.isEmpty() || logo.isEmpty() || thumbnail.isEmpty()){
            Toast.makeText(this, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentProvince == null){
            Toast.makeText(this, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
            return;
        }

        String user = Preferences.getData(Key.USER, this);
        if (user.equals("")){
            Toast.makeText(this, getString(R.string.login_please), Toast.LENGTH_SHORT).show();
            return;
        }

        this.thumbnail = thumbnail;

        User u = new Gson().fromJson(user, User.class);

        Restaurant restaurant = new Restaurant(name, DateTime.getCurrentDate(), open, close, address, phone, web, logo, 0, currentProvince);

        postRestaurant(restaurant, u);
        u.setPermission(1);
        updateRequest(u);


    }

    private List<Province> provinces = new ArrayList<>();
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
                        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(RegisterRestaurantActivity.this, android.R.layout.simple_list_item_1, provinceStr);
                        spinnerProvince.setAdapter(itemAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterRestaurantActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private Province currentProvince;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0){
            currentProvince = provinces.get(position - 1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void postRestaurant(final Restaurant restaurant, final User user) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(restaurant));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.POST_RESTAURANT, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Manager manager = new Manager(user, new Gson().fromJson(response.getJSONObject("data").getJSONObject("restaurant").toString(), Restaurant.class));
                        postImage(new Image(thumbnail, new Gson().fromJson(response.getJSONObject("data").getJSONObject("restaurant").toString(), Restaurant.class)));
                        postManager(manager);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterRestaurantActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postImage(final Image image) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(image));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.POST_IMAGE, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterRestaurantActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void postManager(Manager manager) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(manager));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.POST_MANAGER, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(RegisterRestaurantActivity.this, "Đăng ký thành công, vui lòng đăng nhập bằng Share Food Management", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterRestaurantActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateRequest(User currentUser){
        String json = new Gson().toJson(currentUser);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.LOGIN, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){

                        }else {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterRestaurantActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
