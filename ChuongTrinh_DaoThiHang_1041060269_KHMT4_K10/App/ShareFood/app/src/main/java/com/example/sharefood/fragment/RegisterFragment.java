package com.example.sharefood.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.LoginRegisterActivity;
import com.example.sharefood.R;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.User;
import com.example.sharefood.util.DateTime;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText edtUsername, edtPass, edtName, edtPhone, edtAddress;
    private Button btnRegister;
    private LoginRegisterActivity loginRegisterActivity;
    private RequestQueue requestQueue;
    private RadioButton radMale, radFemale;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        edtUsername = view.findViewById(R.id.edtUserNameRegister);
        edtPass = view.findViewById(R.id.edtPasswordRegister);
        edtName = view.findViewById(R.id.edtNameRegister);
        edtPhone = view.findViewById(R.id.edtPhoneRegister);
        edtAddress = view.findViewById(R.id.edtAddressRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
        radMale = view.findViewById(R.id.radMale);
        radFemale = view.findViewById(R.id.radFemale);
    }

    private void initViews() {
        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(loginRegisterActivity).getmRequestQueue();

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = edtUsername.getText().toString();
        String pass = edtPass.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String address = edtAddress.getText().toString();

        if (username.isEmpty() || pass.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()){
            Toast.makeText(loginRegisterActivity, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = radMale.isChecked()?getString(R.string.male):getString(R.string.female);

        User user = new User(username, pass, name, phone, address, "https://www.zimphysio.org.zw/wp-content/uploads/2018/01/default-avatar-2-300x300.jpg"
            , gender, DateTime.getCurrentDate(), 0, 0);

        register(user);

    }

    private void register(User user) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(user));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.LOGIN, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        String dataStr = response.getJSONObject("data").get("user").toString();

                        if (dataStr.contains("null")){
                            Toast.makeText(loginRegisterActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(loginRegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            final User user = new Gson().fromJson(dataStr, User.class);
                            Preferences.saveData(Key.USER, dataStr, getContext());

                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
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
}
