package com.example.onlineshopstoreapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.onlineshopstoreapp.Domain.BannerModel;
import com.example.onlineshopstoreapp.R;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannnerViewholder> {
    private ArrayList<BannerModel> SliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SliderItems.addAll(SliderItems);
            notifyDataSetChanged();

        }

    };

    public BannerAdapter(ArrayList<BannerModel> sliderItems, ViewPager2 viewPager2) {
        SliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BannnerViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new BannnerViewholder(LayoutInflater.from(context).inflate(R.layout.banner_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannnerViewholder holder, int position) {
    holder.setImage(SliderItems.get(position));
    if (position == SliderItems.size() - 2) {
        viewPager2.post(runnable);
    }
    }

    @Override
    public int getItemCount() {
        return SliderItems.size();
    }

    public class BannnerViewholder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public BannnerViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
        void setImage(BannerModel bannerModel) {
            Glide.with(context).load(bannerModel.getUrl()).into(imageView);
        }
    }
}


