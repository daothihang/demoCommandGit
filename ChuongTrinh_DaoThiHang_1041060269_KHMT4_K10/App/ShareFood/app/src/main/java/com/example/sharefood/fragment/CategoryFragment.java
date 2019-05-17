package com.example.sharefood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.sharefood.CategoryActivity;
import com.example.sharefood.R;
import com.example.sharefood.constant.API;
import com.example.sharefood.model.Category;
import com.example.sharefood.view.CustomItalyTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFragment extends Fragment implements View.OnClickListener {

    private Category currentCategory;
    private CircleImageView imgLogo;
    private CustomItalyTextView tvName;
    private Button btnView;
    private CategoryActivity categoryActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        currentCategory = (Category) bundle.getSerializable("data");
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        findId(view);
        initViews();
        // Inflate the layout for this fragment
        return view;
    }

    private void findId(View view) {
        imgLogo = view.findViewById(R.id.logoCategory);
        tvName = view.findViewById(R.id.tvNameCategory);
        btnView = view.findViewById(R.id.btnViewFood);
    }

    private void initViews() {
        categoryActivity = (CategoryActivity) getActivity();
        String url = currentCategory.getImage().contains("http")?currentCategory.getImage(): API.STORAGE + currentCategory.getImage();
        Glide.with(categoryActivity).load(url).into(imgLogo);

        tvName.setText(currentCategory.getName());

        btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        categoryActivity.switchActivity(currentCategory.getId_category());
    }
}
