package com.example.sharefoodmanagement.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.sharefoodmanagement.MainAdminActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.adapter.UserAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.model.User;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.example.sharefoodmanagement.view.CustomItalyTextView;
import com.example.sharefoodmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserManagerFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private CustomListView lvUser, lvManager;
    private UserAdapter userAdapter, managerAdapter;
    private RequestQueue requestQueue;
    private MainAdminActivity mainAdminActivity;
    private List<User> users, managers;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        lvUser = view.findViewById(R.id.lvUsers);
        lvManager = view.findViewById(R.id.lvManager);
    }

    private void initViews() {
        mainAdminActivity = (MainAdminActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        users = new ArrayList<>();
        managers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), R.layout.item_user, users);
        managerAdapter = new UserAdapter(getContext(), R.layout.item_user, managers);
        lvManager.setAdapter(managerAdapter);
        lvUser.setAdapter(userAdapter);

        getUser();
        getManager();

        lvUser.setOnItemClickListener(this);
        lvManager.setOnItemClickListener(this);
        lvManager.setOnItemLongClickListener(this);
        lvUser.setOnItemLongClickListener(this);
    }

    private void getUser() {
        users.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_USER + "?permission=0&lock=0", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray dataStr = response.getJSONObject("data").getJSONArray("users");

                        if (dataStr.length() == 0){
//                            Toast.makeText(mainAdminActivity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i=0; i<dataStr.length(); i++){
                                User user = new Gson().fromJson(dataStr.getJSONObject(i).toString(), User.class);
                                users.add(user);
                            }

                            userAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getManager() {
        managers.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_USER + "?permission=1&lock=0", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray dataStr = response.getJSONObject("data").getJSONArray("users");

                        if (dataStr.length() == 0){
//                            Toast.makeText(mainAdminActivity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i=0; i<dataStr.length(); i++){
                                User user = new Gson().fromJson(dataStr.getJSONObject(i).toString(), User.class);
                                managers.add(user);
                            }

                            managerAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void initDialogUserInfo(User user){
        dialog = new Dialog(mainAdminActivity,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        else {
//            Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_user_info);
        dialog.setCancelable(true);

        CircleImageView avatar = dialog.findViewById(R.id.imgAvatrUserInfo);
        CustomItalyTextView tvName = dialog.findViewById(R.id.tvNameUserInfo);
        CustomItalyTextView tvUserName = dialog.findViewById(R.id.tvUserNameUserInfo);
        CustomItalyTextView tvPass = dialog.findViewById(R.id.tvPassUserInfo);
        CustomItalyTextView tvPhone = dialog.findViewById(R.id.tvPhoneUserInfo);
        CustomItalyTextView tvGender = dialog.findViewById(R.id.tvGenderUserInfo);
        CustomItalyTextView tvAddress = dialog.findViewById(R.id.tvAddessUserInfo);
        CustomItalyTextView tvPermission = dialog.findViewById(R.id.tvPermissionUserInfo);
        CustomItalyTextView tvLock = dialog.findViewById(R.id.tvLockUserInfo);
        CustomItalyTextView tvCreated = dialog.findViewById(R.id.tvCreated_atUserInfo);

        String url = user.getAvatar().contains("http")?user.getAvatar(): API.STORAGE + user.getAvatar();
        Glide.with(mainAdminActivity).load(url).into(avatar);

        tvName.setText(user.getName());
        tvUserName.setText("Tên tài khoản: " + user.getUser_name());
        tvPass.setText("Mật khẩu: " + user.getPassword());
        tvPhone.setText("Số diện thoại: " + user.getPhone());
        tvGender.setText("Giới tính: " + user.getGender());
        tvAddress.setText("Địa chỉ: " + user.getAddress());
        tvCreated.setText("Ngày lập: " + user.getCreated_at());
        if (user.getPermission() == 0){
            tvPermission.setText("Quyền truy cập: Người dùng");
        }else {
            tvPermission.setText("Quyền truy cập: Quản lý");
        }

        if (user.getLocked() == 0){
            tvLock.setText("Tình trạng: Hoạt động");
        }else {
            tvLock.setText("Tình trạng: Khóa");
        }

        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lvManager:
                initDialogUserInfo(managers.get(position));
                break;
            case R.id.lvUsers:
                initDialogUserInfo(users.get(position));
                break;
        }
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
                                Toast.makeText(mainAdminActivity, getString(R.string.success), Toast.LENGTH_SHORT).show();

                            getUser();
                            getManager();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainAdminActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()){
            case R.id.lvManager:
                User user = managers.get(position);
                if (user.getLocked() == 0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mainAdminActivity);
                    builder.setMessage("Bạn muốn khóa tài khoản này?")
                            .setCancelable(false)
                            .setPositiveButton("Khóa", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    User u = managers.get(position);
                                    u.setLocked(1);
                                    updateRequest(u);
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
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mainAdminActivity);
                    builder.setMessage("Bạn muốn mở khóa tài khoản này?")
                            .setCancelable(false)
                            .setPositiveButton("Mở", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    User u = managers.get(position);
                                    u.setLocked(0);
                                    updateRequest(u);
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
                }

                break;
            case R.id.lvUsers:
                User user1 = users.get(position);
                if (user1.getLocked() == 0){
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainAdminActivity);
                    builder1.setMessage("Bạn muốn khóa tài khoản này?")
                            .setCancelable(false)
                            .setPositiveButton("Khóa", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    User u = users.get(position);
                                    u.setLocked(1);
                                    updateRequest(u);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert1 = builder1.create();
                    alert1.show();
                }else {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainAdminActivity);
                    builder1.setMessage("Bạn muốn mở khóa tài khoản này?")
                            .setCancelable(false)
                            .setPositiveButton("Mở", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    User u = users.get(position);
                                    u.setLocked(0);
                                    updateRequest(u);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert1 = builder1.create();
                    alert1.show();
                }

                break;
        }
        return true;
    }
}
