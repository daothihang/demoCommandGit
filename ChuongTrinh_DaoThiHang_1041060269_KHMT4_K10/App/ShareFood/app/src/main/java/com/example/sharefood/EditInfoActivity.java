package com.example.sharefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Manager;
import com.example.sharefood.model.User;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditInfoActivity extends AppCompatActivity {
    private EditText editPassword;
    private EditText editUserName;
    private EditText editName;
    private EditText editAddress;
    private EditText editPhone;
    private Button btnUpdate;
    private Button btnCancle;
    private RequestQueue requestQueue;
    private List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        initView();
    }

    private void initView() {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getmRequestQueue();
        users = new ArrayList<>();

        editAddress = findViewById(R.id.edtAddressUpdate);
        editName = findViewById(R.id.edtNameUpdate);
        editPassword = findViewById(R.id.edtPasswordUpdate);
        editPhone = findViewById(R.id.edtPhoneUpdate);
        editUserName = findViewById(R.id.edtUserNameUpdate);
        btnUpdate = findViewById(R.id.btnComfirmUpdate);
        btnCancle = findViewById(R.id.btnCancleUpdate);
        final String userStr = Preferences.getData(Key.USER, EditInfoActivity.this);

        try {
            JSONObject jsonObject = new JSONObject(userStr);
            String password = jsonObject.getString("password");
            String userName = jsonObject.getString("user_name");
            String address = jsonObject.getString("address");
            String name = jsonObject.getString("name");
            String avatar = jsonObject.getString("avatar");
            String phone = jsonObject.getString("phone");
            String gender = jsonObject.getString("gender");
            String created_at = jsonObject.getString("created_at");
            int permission = jsonObject.getInt("permission");
            int locked = jsonObject.getInt("locked");

            editUserName.setText(userName);
            editPassword.setText(password);
            editName.setText(name);
            editPhone.setText(phone);
            editAddress.setText(address);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.setUser_name(editUserName.getText().toString().trim());
                    user.setPassword(editPassword.getText().toString().trim());
                    user.setName(editName.getText().toString().trim());
                    user.setPhone(editPhone.getText().toString().trim());
                    user.setAddress(editAddress.getText().toString().trim());
                    user.setAvatar(avatar);
                    user.setGender(gender);
                    user.setCreated_at(created_at);
                    user.setPermission(permission);
                    user.setLocked(locked);
                    user.setId(9);
                    updateUser(user);
                    finish();

                }
            });
            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void updateUser(User user) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(user));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.LOGIN, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(EditInfoActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
