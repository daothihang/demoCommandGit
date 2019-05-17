package com.example.sharefood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.sharefood.adapter.CommentAdapter;
import com.example.sharefood.constant.API;
import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Cart;
import com.example.sharefood.model.Favorite;
import com.example.sharefood.model.FoodComment;
import com.example.sharefood.model.Price;
import com.example.sharefood.model.User;
import com.example.sharefood.util.DateTime;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.VolleySingleton;
import com.example.sharefood.view.CustomItalyTextView;
import com.example.sharefood.view.CustomListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

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
import java.util.Locale;
import java.util.Objects;

public class FoodDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imLogoFood;
    private CustomItalyTextView tvFoodName, tvResName, tvTime, tvAddress, tvWebsite, tvFoodInfo, tvRecipe, tvPrice;
    private Button btnCall;
    private Price currentPrice;

    private GoogleMap mMap;
    private Marker myMarker;
    private Geocoder geocoder;
    private LatLng currentLocation;

    private Button btnSendComment;
    private EditText edtComment;
    private RequestQueue requestQueue;
    private CustomListView lvComment;
    private List<FoodComment> comments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        findId();
        initViews();
        loadData();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarFoodDetail);
        imLogoFood = findViewById(R.id.imgLogoFoodDetail);
        tvFoodName = findViewById(R.id.tvNameFoodDetail);
        tvResName = findViewById(R.id.tvNameResFoodDetail);
        tvTime = findViewById(R.id.tvTimeOpenFoodDetail);
        tvAddress = findViewById(R.id.tvAddressFoodDetail);
        tvWebsite = findViewById(R.id.tvWebsiteFoodDetail);
        tvFoodInfo = findViewById(R.id.tvFoodInfoFoodDetail);
        tvRecipe = findViewById(R.id.tvFoodRecipeFoodDetail);
        btnCall = findViewById(R.id.btnCallFoodDetail);
        tvPrice = findViewById(R.id.tvPriceFoodDetail);
        edtComment = findViewById(R.id.edtComment);
        btnSendComment = findViewById(R.id.btnSendComment);
        lvComment = findViewById(R.id.lvFoodComment);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        btnSendComment.setOnClickListener(this);

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, R.layout.item_comment, comments);
        lvComment.setAdapter(commentAdapter);

        btnCall.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fav) {
            Favorite favorite = new Gson().fromJson(Preferences.getData(Key.FAV, FoodDetailActivity.this), Favorite.class);
            if (favorite != null){
                boolean check  = favorite.isExists(currentPrice.getId_price());
                if (check){
                    Toast.makeText(this, "Món ăn này đã trong danh sách yêu thích của bạn.", Toast.LENGTH_SHORT).show();
                }else {
                    favorite.insert(currentPrice);
                    Preferences.saveData(Key.FAV, new Gson().toJson(favorite), FoodDetailActivity.this);
                    Toast.makeText(this, "Thêm vào danh sách yêu thích thành công.", Toast.LENGTH_SHORT).show();
                }
            }


            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String API_KEY = "MY_API_KEY";
    private void loadData() {
        Intent intent = getIntent();
        currentPrice = (Price) intent.getSerializableExtra(Key.FOOD);

        //check ngôn ngữ thiết bị
        String locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayName();
//        Translator translate = Translator.getInstance();
//        String text2 = translate.translate(text, Language.TURKISH,
//                Language.ENGLISH);


        Glide.with(this).load(currentPrice.getFood().getImage()).into(imLogoFood);
        tvFoodName.setText(currentPrice.getFood().getName().trim());
        tvResName.setText(currentPrice.getRestaurant().getName().trim());
        tvTime.setText(currentPrice.getRestaurant().getTime_open().trim() + " - " + currentPrice.getRestaurant().getTime_close().trim());
        tvAddress.setText(currentPrice.getRestaurant().getAddress().trim());
        tvWebsite.setText(currentPrice.getRestaurant().getWebsite().trim());
        tvRecipe.setText(currentPrice.getFood().getRecipe().trim());
        tvPrice.setText(currentPrice.getPrice() + " VND");

//        translatedText = LanguageApp.translate('en', 'iw', sourceText);
//        urllog = "https://translate.googleapis.com/translate_a/single?client=gtx&sl="
//                + "en" + "&tl=" + "iw" + "&dt=t&q=" + encodeURI(text);

//        TranslateOptions options = TranslateOptions.newBuilder()
//                .setApiKey(API_KEY)
//                .build();
//        Translate translate = options.getService();
//        final Translation translation =
//                translate.translate(""+currentPrice.getFood().getRecipe().trim(),
//                        Translate.TranslateOption.targetLanguage("en"));
        tvFoodInfo.setText(currentPrice.getFood().getRecipe().trim());
        getComment();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String[] permissionList = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissionList) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissionList, 0);
                    return;
                }
            }
        }
        initMap();

    }

    private void initMap() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        LatLng begin;
        try {
            begin = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        } catch (Exception e) {
            begin = new LatLng(21.0544494, 105.735142);
        }
        currentLocation = begin;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(begin));
        CameraPosition position = new CameraPosition(begin, 14, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        geocoder = new Geocoder(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                if (isMove){
//                    currentLocation = mMap.getCameraPosition().target;
//                    drawCircle(currentLocation, currentRadius);
//                }
//            }
//        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });

        AddressUtil addressUtil = new AddressUtil();
        addressUtil.execute(currentPrice.getRestaurant().getAddress());


    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendComment:
                String comment = edtComment.getText().toString();
                if (comment.isEmpty()) {
                    return;
                }

                String user = Preferences.getData(Key.USER, this);
                if (user.equals("")) {
                    Toast.makeText(this, getString(R.string.login_please), Toast.LENGTH_SHORT).show();
                    return;
                }

                User u = new Gson().fromJson(user, User.class);

                FoodComment foodComment = new FoodComment(comment, DateTime.getCurrentDate(), u, currentPrice.getFood());
                postComment(foodComment);

                edtComment.setText("");
                break;
            case R.id.btnCallFoodDetail:
                String number = currentPrice.getRestaurant().getPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                break;
        }
    }


    private class AddressUtil extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... strings) {
            Log.e("address", strings[0]);
            return getLatLng(strings[0]);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            if (latLng == null) {
                Toast.makeText(FoodDetailActivity.this, getString(R.string.check_connect), Toast.LENGTH_SHORT).show();
            } else {
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraPosition position = new CameraPosition(latLng, 14, 0, 0);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(currentPrice.getRestaurant().getName());
                options.snippet(currentPrice.getFood().getName());
                mMap.addMarker(options);
            }
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
        return new LatLng(lat, lng);
    }

    private void getComment() {
        comments.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_COMMENT + "?id_food=" + currentPrice.getFood().getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray data = response.getJSONObject("data").getJSONArray("foodComments");
                        for (int i = 0; i < data.length(); i++) {
                            FoodComment foodComment = new Gson().fromJson(data.getJSONObject(i).toString(), FoodComment.class);
                            comments.add(foodComment);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postComment(FoodComment comment) {

        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(comment));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        comments.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.POST_COMMENT, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        getComment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
