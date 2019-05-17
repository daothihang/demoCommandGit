package com.example.sharefoodmanagement.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefoodmanagement.MainAdminActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.adapter.RestaurantAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.model.Restaurant;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantManagerFragment extends Fragment implements  SearchView.OnQueryTextListener{

    private MainAdminActivity mainAdminActivity;
    private GridView gvRestaurant;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurants;
    private RequestQueue requestQueue;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        gvRestaurant = view.findViewById(R.id.gvRestaurant);
        searchView = view.findViewById(R.id.searchViewCompany);
    }

    private void initViews() {
        mainAdminActivity = (MainAdminActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        restaurants = new ArrayList<>();
        adapter = new RestaurantAdapter(getContext(), R.layout.item_gv_restaurant, restaurants);
        gvRestaurant.setAdapter(adapter);
        gvRestaurant.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
//        loadRestaurant();
    }

    private void loadRestaurant(){
        restaurants.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_RESTAURANT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray dataStr = response.getJSONObject("data").getJSONArray("restaurants");

                        if (dataStr.length() == 0){
                            Toast.makeText(mainAdminActivity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i=0; i<dataStr.length(); i++){
                                Restaurant restaurant = new Gson().fromJson(dataStr.getJSONObject(i).toString(), Restaurant.class);
                                restaurants.add(restaurant);
                            }

                            adapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        if (adapter!=null)
            loadRestaurant();
    }

    @Override
    public boolean onQueryTextSubmit(String string) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String string) {
        if (TextUtils.isEmpty(string)) {
            gvRestaurant.clearTextFilter();
        } else {
            gvRestaurant.setFilterText(string);
        }
        return false;
    }
}
