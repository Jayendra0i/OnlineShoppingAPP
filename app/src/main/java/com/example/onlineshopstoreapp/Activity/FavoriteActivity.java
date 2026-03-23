package com.example.onlineshopstoreapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.onlineshopstoreapp.Adapter.PopularAdapter;
import com.example.onlineshopstoreapp.Helper.ManagmentFavorite;
import com.example.onlineshopstoreapp.databinding.ActivityFavoriteBinding;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;
    private ManagmentFavorite managmentFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentFavorite = new ManagmentFavorite(this);

        setVariable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFavoriteList();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initFavoriteList() {
        if (managmentFavorite.getListFavorite().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.favoriteView.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.favoriteView.setVisibility(View.VISIBLE);

            binding.favoriteView.setLayoutManager(new GridLayoutManager(this, 2));
            binding.favoriteView.setAdapter(new PopularAdapter(managmentFavorite.getListFavorite()));
        }
    }
}
