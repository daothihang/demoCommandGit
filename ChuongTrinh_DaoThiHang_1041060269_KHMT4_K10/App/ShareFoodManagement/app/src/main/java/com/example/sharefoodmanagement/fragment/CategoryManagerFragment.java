package com.example.sharefoodmanagement.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sharefoodmanagement.MainAdminActivity;
import com.example.sharefoodmanagement.R;
import com.example.sharefoodmanagement.adapter.CategoryAdapter;
import com.example.sharefoodmanagement.constant.API;
import com.example.sharefoodmanagement.model.Category;
import com.example.sharefoodmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private MainAdminActivity mainAdminActivity;
    private GridView gvCategory;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private RequestQueue requestQueue;
    private FloatingActionButton btnAdd;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        gvCategory = view.findViewById(R.id.gvCategory);
        btnAdd = view.findViewById(R.id.btnAddCategory);
    }

    private void initViews() {
        mainAdminActivity = (MainAdminActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        categories = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), R.layout.item_gv_restaurant, categories);
        gvCategory.setAdapter(adapter);
        btnAdd.setOnClickListener(this);
        gvCategory.setOnItemClickListener(this);

//        getCategory();
    }

    private void getCategory() {
        categories.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_CATEGORY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray dataStr = response.getJSONObject("data").getJSONArray("categories");

                        if (dataStr.length() == 0) {
                            Toast.makeText(mainAdminActivity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < dataStr.length(); i++) {
                                Category category = new Gson().fromJson(dataStr.getJSONObject(i).toString(), Category.class);
                                categories.add(category);
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
        if (adapter != null) {
            getCategory();
        }
    }

    @Override
    public void onClick(View v) {
        initDialogAddCategory();
    }

    private void initDialogAddCategory() {
        dialog = new Dialog(mainAdminActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_add_category);
        dialog.setCancelable(true);

        final EditText edtName = dialog.findViewById(R.id.edtCategoryName);
        final EditText edtImage = dialog.findViewById(R.id.edtCategoryImage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String image = edtImage.getText().toString();

                if (name.isEmpty() || image.isEmpty()) {
                    Toast.makeText(mainAdminActivity, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!image.contains("http")) {
                    Toast.makeText(mainAdminActivity, getString(R.string.image_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                insertCategory(name, image);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initDialogUpdateCategory(final Category category) {
        dialog = new Dialog(mainAdminActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_update_category);
        dialog.setCancelable(true);

        final EditText edtName = dialog.findViewById(R.id.edtCategoryName);
        final EditText edtImage = dialog.findViewById(R.id.edtCategoryImage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        edtImage.setText(category.getImage());
        edtName.setText(category.getName());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String image = edtImage.getText().toString();

                if (name.isEmpty() || image.isEmpty()) {
                    Toast.makeText(mainAdminActivity, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!image.contains("http")) {
                    Toast.makeText(mainAdminActivity, getString(R.string.image_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                category.setImage(image);
                category.setName(name);

                updateCategory(category);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void insertCategory(String name, String image) {
        Category category = new Category(name, image);
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(category));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.GET_ALL_CATEGORY, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainAdminActivity, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                        getCategory();
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.updateCatgory:
                        initDialogUpdateCategory(categories.get(position));
                        break;
                    case R.id.deleteCategory:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mainAdminActivity);
                        builder.setMessage("Bạn muốn xóa mục này?")
                                .setCancelable(false)
                                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        deleteCategory(categories.get(position).getId_category());
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
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private void updateCategory(Category category) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(category));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, API.GET_ALL_CATEGORY, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mainAdminActivity, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        getCategory();
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

    private void deleteCategory(int id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, API.GET_ALL_CATEGORY + "?id_category=" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if (response.toString().contains("true")) {
                    Toast.makeText(mainAdminActivity, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    getCategory();
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
}
