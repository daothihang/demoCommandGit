package com.example.sharefood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefood.adapter.PriceAdapter;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Cart;
import com.example.sharefood.model.Price;
import com.example.sharefood.model.ShoppingCart;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.ProcessDialog;
import com.example.sharefood.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PriceAdapter.OnClickItemListener {

    private RecyclerView recyclerViewFood;
    private PriceAdapter foodAdapter;
    private List<Price> foods = new ArrayList<>();
    private RequestQueue requestQueue;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int allFoods = 0;
    private int currentPage = 1;
    private Toolbar toolbar;
    private ImageView imMenu, imShoppingCart;
    private TextView tvCountShoppingCart;
    private Dialog dialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ShoppingCart shoppingCart = new ShoppingCart();
    private ProcessDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findId();
        initViews();
        loadShoppingCart();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    private void loadShoppingCart() {
        String shopping = Preferences.getData(Key.SHOPPING_CART, this);
        if (!shopping.equals("")){
            shoppingCart = new Gson().fromJson(shopping, ShoppingCart.class);
            int countItem = shoppingCart.getCarts().size();
            if (countItem <= 99){
                tvCountShoppingCart.setText(String.valueOf(countItem));
            }else {
                tvCountShoppingCart.setText("1+");
            }
        }else {
            tvCountShoppingCart.setText(String.valueOf(0));
        }
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarMain);
        recyclerViewFood = findViewById(R.id.recyclerFood);
        shimmerFrameLayout = findViewById(R.id.shimmerFood);
        imMenu = findViewById(R.id.imMenu);
        imShoppingCart = findViewById(R.id.imShoppingCart);
        tvCountShoppingCart = findViewById(R.id.tvCountItemCart);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new PriceAdapter(recyclerViewFood, this, foods);
        recyclerViewFood.setAdapter(foodAdapter);
        foodAdapter.setOnItemClickListener(this);
        shimmerFrameLayout.startShimmer();
        processDialog = new ProcessDialog(this);
        countAllFood();

        loadMoreData(currentPage);

        //Set Load more event
        foodAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (foods.size() <= allFoods) // Change max size
                {
                    foods.add(null);
                    foodAdapter.notifyItemInserted(foods.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            foods.remove(foods.size() - 1);
                            foodAdapter.notifyItemRemoved(foods.size());
                            currentPage++;
                            loadMoreData(currentPage);

                        }
                    }, 3000); // Time to load
                } else {
                    Toast.makeText(MainActivity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imMenu.setOnClickListener(this);
        imShoppingCart.setOnClickListener(this);

    }

    private void loadMoreData(final int page) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.PRICE_WITH_PAGE + "?page=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("prices");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Price price = (Price) gson.fromJson(jsonArray.getJSONObject(i).toString(), Price.class);
                            foods.add(price);
                        }
                        if (shimmerFrameLayout.isShimmerStarted()) {
                            CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                }
                            }.start();

                        }
                        foodAdapter.notifyDataSetChanged();
                        foodAdapter.setLoaded();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void countAllFood() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.COUNT_PRICE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject data = response.getJSONObject("data");
                        allFoods = data.getInt("count");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imMenu:
                initDialogMenu();
                break;
            case R.id.imShoppingCart:
                if (shoppingCart.getCarts().size() == 0){
                    Toast.makeText(this, getString(R.string.count_none), Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(this, ShoppingCartActivity.class));

                break;
        }
    }

    @SuppressLint("NewApi")
    private void initDialogMenu() {
        dialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_menu);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        Button btnCategory = dialog.findViewById(R.id.btnCategory);
//        Button btnMap = dialog.findViewById(R.id.btnMap);
        Button btnRegister = dialog.findViewById(R.id.btnResgisterRestaurant);
        Button btnLogin = dialog.findViewById(R.id.btnLogin);
//        Button btnRestaurant = dialog.findViewById(R.id.btnRestaurant);

        dialog.show();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            return p1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected LatLng getLatLng(String address) {
        String key = Uri.encode(address);
        String uri = API.API_LOCATION + key;
        Log.e("API", uri);

        HttpGet httpGet = new HttpGet(uri);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int byteData;
            while ((byteData = stream.read()) != -1) {
                stringBuilder.append((char) byteData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            lng = jsonObject.getJSONObject("Response").getJSONArray("View").getJSONObject(0).getJSONArray("Result").getJSONObject(0).getJSONObject("Location").getJSONObject("DisplayPosition").getDouble("Longitude");
            lat = jsonObject.getJSONObject("Response").getJSONArray("View").getJSONObject(0).getJSONArray("Result").getJSONObject(0).getJSONObject("Location").getJSONObject("DisplayPosition").getDouble("Latitude");
            Log.e("lat-lng", lat + " - " + lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LatLng(lat,lng);
    }

    public double distanceBetweenTwoPoint(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Preferences.saveData(Key.CURRENT_LOCATION, String.valueOf(mLastLocation.getLatitude())+ "<>" + String.valueOf(mLastLocation.getLongitude()), MainActivity.this);
            Log.e("Lat", String.valueOf(mLastLocation.getLatitude()));
            Log.e("Long", String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.check_connect), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onButtonClick(View view, int position) {
        processDialog.show();
        AddressUtil addressUtil = new AddressUtil();
        addressUtil.setPrice(foods.get(position));
        addressUtil.execute(foods.get(position).getRestaurant().getAddress());
    }

    private class AddressUtil extends AsyncTask<String, Void, LatLng>{

        private Price price;

        public void setPrice(Price price){
            this.price = price;
        }

        @Override
        protected LatLng doInBackground(String... strings) {
            Log.e("address", strings[0]);
            return getLatLng(strings[0]);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            if (latLng == null){
                Toast.makeText(MainActivity.this, getString(R.string.check_connect), Toast.LENGTH_SHORT).show();
            }else {
                if (mLastLocation == null){
                    Toast.makeText(MainActivity.this, getString(R.string.check_connect), Toast.LENGTH_SHORT).show();
                }else {
                    double distance = distanceBetweenTwoPoint(latLng.latitude, latLng.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    DecimalFormat df = new DecimalFormat(".##");

                    Log.e("distance", df.format(distance) + "");
                    if (distance <= Key.DISTANCE_CONFIG){
                        Cart cart = new Cart(price, 1, price.getPrice());
                        shoppingCart.addCart(cart);
                        Preferences.saveData(Key.SHOPPING_CART, new Gson().toJson(shoppingCart), MainActivity.this);
                        loadShoppingCart();
                        Toast.makeText(MainActivity.this, getString(R.string.added) + " " + price.getFood().getName() + " " + getString(R.string.into_cart), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, getString(R.string.distance_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (processDialog.isShow()){
                processDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShoppingCart();
    }
}
