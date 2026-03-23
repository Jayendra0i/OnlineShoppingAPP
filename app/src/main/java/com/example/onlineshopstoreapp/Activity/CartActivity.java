package com.example.onlineshopstoreapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlineshopstoreapp.Adapter.CartAdapter;
import com.example.onlineshopstoreapp.Helper.ManagmentCart;
import com.example.onlineshopstoreapp.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        initCartList();
        calculateCart();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.checkoutBtn.setOnClickListener(v -> {
            if (managmentCart.getListCart().isEmpty()) {
                Toast.makeText(CartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Your order has been placed successfully!", Toast.LENGTH_LONG).show();
                // Optionally clear the cart after checkout
                // managmentCart.clearCart();
                // initCartList();
                // calculateCart();
            }
        });
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.cartView.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.cartView.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> {
            calculateCart();
            if (managmentCart.getListCart().isEmpty()) {
                binding.emptyTxt.setVisibility(View.VISIBLE);
                binding.cartView.setVisibility(View.GONE);
            }
        }));
    }

    private void calculateCart() {
        double percentTax = 0.02; // 2% tax
        double delivery = 10; // $10 delivery fee

        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}
