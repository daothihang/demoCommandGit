package com.example.sharefood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sharefood.adapter.CartAdapter;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Cart;
import com.example.sharefood.model.ShoppingCart;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.view.CustomItalyTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imBack, imRemoveAll;
    private ListView lvCart;
    private CustomItalyTextView tvTotal;
    private Button btnPay;
    private List<Cart> carts;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        findId();
        loadShoppingCart();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarShopping);
        imBack = findViewById(R.id.imBack);
        imRemoveAll = findViewById(R.id.imDeleteAll);
        lvCart = findViewById(R.id.lvCart);
        tvTotal = findViewById(R.id.tvTotalAmount);
        btnPay = findViewById(R.id.btnPay);
    }

    private void initViews() {
        cartAdapter = new CartAdapter(this, R.layout.item_cart, carts, tvTotal);
        lvCart.setAdapter(cartAdapter);
    }

    private void loadShoppingCart() {
        carts = new ArrayList<>();
        ShoppingCart shoppingCart = new Gson().fromJson(Preferences.getData(Key.SHOPPING_CART, this), ShoppingCart.class);
        carts.addAll(shoppingCart.getCarts());
    }
}

