package com.example.onlineshopstoreapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.onlineshopstoreapp.Adapter.BannerAdapter;
import com.example.onlineshopstoreapp.Adapter.CategoryAdapter;
import com.example.onlineshopstoreapp.Adapter.PopularAdapter;
import com.example.onlineshopstoreapp.Domain.BannerModel;
import com.example.onlineshopstoreapp.R;
import com.example.onlineshopstoreapp.Viewmodel.MainViewModel;
import com.example.onlineshopstoreapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private static final int SPEECH_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initCategory();
        initBanner();
        initPopular();
        initBottomNavigation();
        initSearch();

        binding.imageView9.setOnClickListener(v -> Toast.makeText(MainActivity.this, "You have no new notifications", Toast.LENGTH_SHORT).show());
        
        binding.imageView10.setOnClickListener(v -> showSettingsDialog());
    }

    private void showSettingsDialog() {
        String[] options = {"Change Theme", "Change Language (English/Hindi)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                toggleTheme();
            } else if (which == 1) {
                showLanguageDialog();
            }
        });
        builder.show();
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Light Mode Enabled", Toast.LENGTH_SHORT).show();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Hindi"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languages, -1, (dialog, which) -> {
            if (which == 0) {
                setLocale("en");
            } else if (which == 1) {
                setLocale("hi");
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restart activity to apply changes
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initSearch() {
        binding.editTextText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.editTextText.getRight() - binding.editTextText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    startVoiceRecognition();
                    return true;
                }
            }
            return false;
        });
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Voice recognition not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                binding.editTextText.setText(result.get(0));
            }
        }
    }

    private void initBottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.home, true);
        binding.bottomNavigation.setOnItemSelectedListener(i -> {
            if(i==R.id.cart){
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else if (i == R.id.profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (i == R.id.favorite) {
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            }
        });
    }

    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        viewModel.loadPopular().observe(this, itemsModels -> {
            if (itemsModels != null && !itemsModels.isEmpty()) {
                binding.popularView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                binding.popularView.setAdapter(new PopularAdapter(itemsModels));
                binding.popularView.setNestedScrollingEnabled(true);
            }
            binding.progressBarPopular.setVisibility(View.GONE);
        });
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observe(this, categoryModels -> {
            binding.CategoryView.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            binding.CategoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }

    private void banners(ArrayList<BannerModel> bannerModels) {
        binding.viewBanner.setAdapter(new BannerAdapter(bannerModels, binding.viewBanner));
        binding.viewBanner.setClipToPadding(false);
        binding.viewBanner.setClipChildren(false);
        binding.viewBanner.setOffscreenPageLimit(3);
        binding.viewBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewBanner.setPageTransformer(compositePageTransformer);
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        viewModel.loadBanner().observe(this, bannerModels -> {
            if (bannerModels != null && !bannerModels.isEmpty()) {
                banners(bannerModels);
            }
            binding.progressBarBanner.setVisibility(View.GONE);
        });
    }
}
