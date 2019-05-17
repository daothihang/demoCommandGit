package com.example.sharefoodmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.example.sharefoodmanagement.adapter.PagerManagerAdapter;
import com.example.sharefoodmanagement.constant.Key;
import com.example.sharefoodmanagement.model.User;
import com.example.sharefoodmanagement.util.Preferences;
import com.example.sharefoodmanagement.util.ProcessDialog;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.google.gson.Gson;

public class MainManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerManagerAdapter pagerAdapter;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private User currentUser;
    private ImageView imLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarManager);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.sliding_tabs);
        imLogout = findViewById(R.id.imgLogout);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        processDialog = new ProcessDialog(this);
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
        pagerAdapter = new PagerManagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainManagerActivity.this);
                builder.setMessage("Bạn muốn đăng xuất?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                Preferences.saveData(Key.USER, "", MainManagerActivity.this);
                                startActivity(new Intent(MainManagerActivity.this, LoginActivity.class));
                                finish();
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
        });
    }
}
