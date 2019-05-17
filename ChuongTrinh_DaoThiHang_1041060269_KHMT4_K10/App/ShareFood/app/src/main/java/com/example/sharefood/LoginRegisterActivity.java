package com.example.sharefood;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.sharefood.adapter.PagerAdapter;

public class LoginRegisterActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        findId();
        initViews();
    }

    private void findId() {
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private void initViews() {

    }

    @Override
    public void finish() {
        super.finish();
    }
}
