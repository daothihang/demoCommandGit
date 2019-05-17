package com.example.sharefoodmanagement;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.User;
import com.example.sharefoodmanagement.util.Preferences;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUserName, edtPassword;
    private Button btnLogin;
    private RequestQueue requestQueue;
    private TextView tvForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findId();
        initViews();
    }

    private void findId() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword =  findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.forgetPassword);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        btnLogin.setOnClickListener(this);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogForgotPassword();
            }
        });
    }

    @SuppressLint("NewApi")
    private void initDialogForgotPassword() {
        final Dialog dialogComfirm = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogComfirm.setContentView(R.layout.dialog_forgot_password);
        Objects.requireNonNull(dialogComfirm.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogComfirm.setCancelable(true);
        final EditText editComfirmEmail = dialogComfirm.findViewById(R.id.edtEmailForgot);
        EditText editComfirmCode = dialogComfirm.findViewById(R.id.edtCodeForgot);
        TextView tvTitleCode = dialogComfirm.findViewById(R.id.tv_code_comfirm);
        TextView tvTime = dialogComfirm.findViewById(R.id.time);
        Button btnComfirm = dialogComfirm.findViewById(R.id.btnCode);

        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editComfirmEmail.getText().toString();
                if (userName.isEmpty()){
                    Toast.makeText(LoginActivity.this, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                forgotPass(userName);
                dialogComfirm.dismiss();

            }
        });
        dialogComfirm.show();
    }

    private void forgotPass(String username) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.FORGOT_PASS
                + "?user_name=" + username, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        String dataStr = response.getJSONObject("data").get("user").toString();

                        if (dataStr.contains("null")) {
                            Toast.makeText(LoginActivity.this, getString(R.string.forgot_pass_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            final User user = new Gson().fromJson(dataStr, User.class);
                            sendPass(user.getPhone(), user.getPassword());
                            Toast.makeText(LoginActivity.this, getString(R.string.forgot_pass_success), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendPass(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "Mật khẩu truy cập của bạn là: " + message, null, null);
    }

    @Override
    public void onClick(View v) {
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();

        if (userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
            return;
        }

        login(userName, password);

    }

    private void login(String username, String password) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.LOGIN
                + "?username=" + username + "&pass=" + password, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        String dataStr = response.getJSONObject("data").get("user").toString();

                        if (dataStr.contains("null")){
                            Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                        }else {
                            final User user = new Gson().fromJson(dataStr, User.class);
                            if (user.getPermission() == 2){
                                Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                Preferences.saveData(Key.USER, dataStr, LoginActivity.this);

                                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }
                                    @Override
                                    public void onFinish() {
                                        Intent intent = new Intent(LoginActivity.this, MainAdminActivity.class);
                                        intent.putExtra(Key.USER, user);
                                        startActivity(intent);
                                        finish();
                                    }
                                }.start();
                            }else if (user.getPermission() == 1){
                                Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                Preferences.saveData(Key.USER, dataStr, LoginActivity.this);

                                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }
                                    @Override
                                    public void onFinish() {
                                        Intent intent = new Intent(LoginActivity.this, MainManagerActivity.class);
                                        intent.putExtra(Key.USER, user);
                                        startActivity(intent);
                                        finish();
                                    }
                                }.start();
                            }else {
                                Toast.makeText(LoginActivity.this, "Quyền truy cập bị hạn chế", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
