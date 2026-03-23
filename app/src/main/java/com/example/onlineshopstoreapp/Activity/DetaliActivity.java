package com.example.onlineshopstoreapp.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlineshopstoreapp.Adapter.ColorAdapter;
import com.example.onlineshopstoreapp.Adapter.SizeAdapter;
import com.example.onlineshopstoreapp.Adapter.picListAdapter;
import com.example.onlineshopstoreapp.Domain.ItemsModel;
import com.example.onlineshopstoreapp.Helper.ManagmentCart;
import com.example.onlineshopstoreapp.Helper.ManagmentFavorite;
import com.example.onlineshopstoreapp.R;
import com.example.onlineshopstoreapp.databinding.ActivityDetaliBinding;

import java.util.ArrayList;

public class DetaliActivity extends AppCompatActivity {
    private ActivityDetaliBinding binding;
    private ItemsModel object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private ManagmentFavorite managmentFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetaliBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        managmentFavorite = new ManagmentFavorite(this);

        getBundle();

        if (object != null) {
            initPicList();
            initSize();
            initColor();
            initFavorite();
        } else {
            Log.e("DetaliActivity", "Object is null, finishing activity");
            finish();
        }

    }

    private void initFavorite() {
        if (managmentFavorite.isFavorite(object)) {
            binding.imageView11.setImageResource(R.drawable.btn_2); // Assuming btn_2 is a filled heart/favorite icon
        } else {
            binding.imageView11.setImageResource(R.drawable.fav); // Assuming fav is an empty heart
        }

        binding.imageView11.setOnClickListener(v -> {
            if (managmentFavorite.isFavorite(object)) {
                managmentFavorite.removeItem(object);
                binding.imageView11.setImageResource(R.drawable.fav);
            } else {
                managmentFavorite.insertItem(object);
                binding.imageView11.setImageResource(R.drawable.btn_2);
            }
        });
    }

    private void initColor() {
        if (object.getColor() != null && !object.getColor().isEmpty()) {
            binding.recyclerColor.setAdapter(new ColorAdapter(object.getColor()));
            binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    private void initSize() {
        if (object.getSize() != null && !object.getSize().isEmpty()) {
            binding.recyclerSize.setAdapter(new SizeAdapter(object.getSize()));
            binding.recyclerSize.setLayoutManager(new
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    private void initPicList() {
        if (object.getPicUrl() != null && !object.getPicUrl().isEmpty()) {
            ArrayList<String> picList = new ArrayList<>(object.getPicUrl());
            Glide.with(this)
                    .load(picList.get(0))
                    .into(binding.pic);
            binding.picList.setAdapter(new picListAdapter(picList, binding.pic));
            binding.picList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    private void getBundle() {
        try {
            object = (ItemsModel) getIntent().getSerializableExtra("object");
            if (object != null) {
                binding.titleTxt.setText(object.getTitle());
                binding.priceTxt.setText("$" + object.getPrice());
                binding.oldPriceTxt.setText("$" + object.getOldPrice());
                binding.oldPriceTxt.setPaintFlags(binding.oldPriceTxt.getPaintFlags() |
                        Paint.STRIKE_THRU_TEXT_FLAG);
                binding.descriptionTxt.setText(object.getDescription());
                binding.addToCartBtn.setOnClickListener(v -> {
                    object.setNumberInCart(numberOrder);
                    managmentCart.insertItem(object);
                });
                binding.backBtn.setOnClickListener(v -> finish());
            }
        } catch (Exception e) {
            Log.e("DetaliActivity", "Error getting bundle: " + e.getMessage());
        }
    }
}
