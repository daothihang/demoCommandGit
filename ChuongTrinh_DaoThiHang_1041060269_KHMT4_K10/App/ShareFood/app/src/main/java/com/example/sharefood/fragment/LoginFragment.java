package com.example.sharefood.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.se.omapi.Session;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.EditInfoActivity;
import com.example.sharefood.FavoriteActivity;
import com.example.sharefood.LoginRegisterActivity;
import com.example.sharefood.Main2Activity;
import com.example.sharefood.R;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.User;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Properties;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private EditText edtUsername, edtPass;
    private Button btnLogin;
    private RequestQueue requestQueue;
    private LoginRegisterActivity loginRegisterActivity;
    private CheckBox checkBox;
    private TextView forgetPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String FREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findId(view);
        initViews();
        sharedPreferences = getContext().getSharedPreferences(FREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkBox = view.findViewById(R.id.checkbox);
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        edtUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        edtPass.setText(sharedPreferences.getString(KEY_PASS, ""));
        edtUsername.addTextChangedListener(this);
        edtPass.addTextChangedListener(this);
        checkBox.setOnCheckedChangeListener(this);

        return view;
    }

    private void managerPrefs() {
        if (checkBox.isChecked()) {
            editor.putString(KEY_USERNAME, edtUsername.getText().toString().trim());
            editor.putString(KEY_PASS, edtPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);
            editor.remove(KEY_USERNAME);
            editor.apply();
        }
    }


    private void findId(View view) {

        edtUsername = view.findViewById(R.id.edtUserName);
        edtPass = view.findViewById(R.id.edtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        forgetPassword = view.findViewById(R.id.forgetPassword);
    }

    private void initViews() {
        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(loginRegisterActivity).getmRequestQueue();

        btnLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogForgotPassword();
            }
        });
    }

    private int dem = 0;
    public int counter;
    private void initDialogForgotPassword() {
        Dialog dialogComfirm = new Dialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialogComfirm.setContentView(R.layout.dialog_forgot_password);
        Objects.requireNonNull(dialogComfirm.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogComfirm.setCancelable(true);
        EditText editComfirmEmail = dialogComfirm.findViewById(R.id.edtEmailForgot);
        EditText editComfirmCode = dialogComfirm.findViewById(R.id.edtCodeForgot);
        TextView tvTitleCode = dialogComfirm.findViewById(R.id.tv_code_comfirm);
        TextView tvTime = dialogComfirm.findViewById(R.id.time);
        Button btnComfirm = dialogComfirm.findViewById(R.id.btnCode);

        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editComfirmEmail.getText().toString();
                if (userName.isEmpty()){
                    Toast.makeText(loginRegisterActivity, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                forgotPass(userName);
                dialogComfirm.dismiss();

            }
        });
        dialogComfirm.show();
    }

    private void sendPass(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "Mật khẩu truy cập của bạn là: " + message, null, null);
    }


    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "daothuhang0696@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(loginRegisterActivity, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        String username = edtUsername.getText().toString();
        String pass = edtPass.getText().toString();

        if (username.isEmpty() || pass.isEmpty()) {
            Toast.makeText(loginRegisterActivity, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
            return;
        }
        login(username, pass);
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
                            Toast.makeText(loginRegisterActivity, getString(R.string.forgot_pass_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            final User user = new Gson().fromJson(dataStr, User.class);
                            sendPass(user.getPhone(), user.getPassword());
                            Toast.makeText(loginRegisterActivity, getString(R.string.forgot_pass_success), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

                        if (dataStr.contains("null")) {
                            Toast.makeText(loginRegisterActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            final User user = new Gson().fromJson(dataStr, User.class);
                            if (user.getPermission() != 0) {
                                Toast.makeText(loginRegisterActivity, "Quyền truy cập bị hạn chế.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(loginRegisterActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

                            Preferences.saveData(Key.USER, dataStr, getContext());

                            CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    loginRegisterActivity.finish();
                                }
                            }.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        managerPrefs();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managerPrefs();
    }
}
