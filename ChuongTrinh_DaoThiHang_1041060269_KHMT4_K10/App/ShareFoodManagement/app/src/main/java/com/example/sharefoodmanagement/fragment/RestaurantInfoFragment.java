package com.example.sharefoodmanagement.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.sharefoodmanagement.MainManagerActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.adapter.ImageAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.Image;
import com.example.sharefoodmanagement.model.Manager;
import com.example.sharefoodmanagement.model.Restaurant;
import com.example.sharefoodmanagement.model.User;
import com.example.sharefoodmanagement.util.Preferences;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.example.sharefoodmanagement.view.CustomItalyTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RestaurantInfoFragment extends Fragment implements View.OnClickListener {

    //    private EditText edtName, edtOpen, edtClose, edtAdress, edtPhone, edtWeb;
//    private Button btnUpdate;
//    private CircleImageView logo;
    private MainManagerActivity mainManagerActivity;
    //    private RequestQueue requestQueue;
    private User currentUser;
    private Manager currentManager;

    private ViewPager viewPager;
    private ImageView imgLogo;
    private CustomItalyTextView tvName, tvOpen, tvAddress, tvPhone, tvWeb, tvProvince, tvStatus;
    private Restaurant currentRestaurant;
    private ImageAdapter slidingAdapter;
    private List<Image> imageSlidings = new ArrayList<>();
    private RequestQueue requestQueue;
    private FloatingActionButton btnEdit;
    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        findId(view);
        initViews();
        // Inflate the layout for this fragment
        return view;
    }

    private void findId(View view) {
//        logo = view.findViewById(R.id.imgLogoRes);
//        edtName = view.findViewById(R.id.edtNameRes);
//        edtOpen = view.findViewById(R.id.edtOpenTimeRes);
//        edtClose = view.findViewById(R.id.edtCloseTimeRes);
//        edtAdress = view.findViewById(R.id.edtAddressRes);
//        edtPhone = view.findViewById(R.id.edtPhoneRes);
//        edtWeb = view.findViewById(R.id.edtWebsiteRes);
//        btnUpdate = view.findViewById(R.id.btnUpdateRes);

        imgLogo = view.findViewById(R.id.imgResInfo);
        tvName = view.findViewById(R.id.tvNameResInfo);
        tvOpen = view.findViewById(R.id.tvOpenTimeResInfo);
        tvAddress = view.findViewById(R.id.tvAddressResInfo);
        tvPhone = view.findViewById(R.id.tvPhoneResInfo);
        tvWeb = view.findViewById(R.id.tvWebsiteResInfo);
        tvProvince = view.findViewById(R.id.tvProvinceResInfo);
        viewPager = view.findViewById(R.id.viewpager_res);
        tvStatus = view.findViewById(R.id.tvStatusResInfo);
        btnEdit = view.findViewById(R.id.btnEditRestaurant);
    }

    private void initViews() {
        mainManagerActivity = (MainManagerActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();

        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainManagerActivity), User.class);

        getManager(currentUser.getId());

        slidingAdapter = new ImageAdapter(mainManagerActivity, imageSlidings);
        viewPager.setAdapter(slidingAdapter);

        btnEdit.setOnClickListener(this);

//        edtClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//

//            }
//        });
//
//        edtOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//

//            }
//        });
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = edtName.getText().toString();
//                String open = edtOpen.getText().toString();
//                String close = edtClose.getText().toString();
//                String address = edtAdress.getText().toString();
//                String phone = edtPhone.getText().toString();
//                String web = edtWeb.getText().toString();
//
//                if (name.isEmpty() || open.isEmpty() || close.isEmpty() || address.isEmpty() || phone.isEmpty() || web.isEmpty()){
//                    Toast.makeText(mainManagerActivity, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Restaurant res = currentManager.getRestaurant();
//                res.setName(name);
//                res.setTime_open(open);
//                res.setTime_close(close);
//                res.setAddress(address);
//                res.setPhone(phone);
//                res.setWebsite(web);
//
//                putRestaurant(res);
//
//            }
//        });

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

                        loadData();
                        getImages();

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

    private void loadData() {
        Glide.with(mainManagerActivity).load(currentManager.getRestaurant().getLogo()).into(imgLogo);
        tvName.setText(currentManager.getRestaurant().getName().trim());
        tvOpen.setText(currentManager.getRestaurant().getTime_open().trim() + " - " + currentManager.getRestaurant().getTime_close().trim());
        tvAddress.setText(currentManager.getRestaurant().getAddress().trim());
        tvPhone.setText(currentManager.getRestaurant().getPhone().trim());
        tvWeb.setText(currentManager.getRestaurant().getWebsite().trim());
        tvProvince.setText(currentManager.getRestaurant().getProvince().getDescription().trim());

        if (currentManager.getRestaurant().getLocked() == 0) {
            tvStatus.setText(getString(R.string.active));
        } else {
            tvStatus.setText(getString(R.string.lock));

        }
    }

    private void getImages() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_IMAGE + "?id_restaurant=" + currentManager.getRestaurant().getId(), null, new Response.Listener<JSONObject>() {
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
                Toast.makeText(mainManagerActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void putRestaurant(final Restaurant restaurant) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(restaurant));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.POST_RESTAURANT, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainManagerActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData();
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
        initDialogEditRes(currentManager.getRestaurant());
    }


    private void initDialogEditRes(Restaurant restaurant) {
        dialog = new Dialog(mainManagerActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_edit_restaurant);
        dialog.setCancelable(true);

        final EditText edtName = dialog.findViewById(R.id.edtNameRes);
        final EditText edtOpen = dialog.findViewById(R.id.edtOpenTimeRes);
        final EditText edtClose = dialog.findViewById(R.id.edtCloseTimeRes);
        final EditText edtAdress = dialog.findViewById(R.id.edtAddressRes);
        final EditText edtPhone = dialog.findViewById(R.id.edtPhoneRes);
        final EditText edtWeb = dialog.findViewById(R.id.edtWebsiteRes);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateRes);

        edtName.setText(restaurant.getName().trim());
        edtOpen.setText(restaurant.getTime_open().trim());
        edtClose.setText(restaurant.getTime_close().trim());
        edtAdress.setText(restaurant.getAddress().trim());
        edtPhone.setText(restaurant.getPhone().trim());
        edtWeb.setText(restaurant.getWebsite().trim());

        edtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mainManagerActivity, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(mainManagerActivity, new TimePickerDialog.OnTimeSetListener() {
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


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String open = edtOpen.getText().toString();
                String close = edtClose.getText().toString();
                String address = edtAdress.getText().toString();
                String phone = edtPhone.getText().toString();
                String web = edtWeb.getText().toString();

                if (name.isEmpty() || open.isEmpty() || close.isEmpty() || address.isEmpty() || phone.isEmpty() || web.isEmpty()) {
                    Toast.makeText(mainManagerActivity, getString(R.string.into_info), Toast.LENGTH_SHORT).show();
                    return;
                }

                Restaurant res = currentManager.getRestaurant();
                res.setName(name);
                res.setTime_open(open);
                res.setTime_close(close);
                res.setAddress(address);
                res.setPhone(phone);
                res.setWebsite(web);

                putRestaurant(res);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
