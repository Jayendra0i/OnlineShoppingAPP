package com.example.onlineshopstoreapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopstoreapp.R;
import com.example.onlineshopstoreapp.databinding.ViewholderPiclistBinding;

import java.util.ArrayList;
import java.util.List;

public class picListAdapter extends RecyclerView.Adapter<picListAdapter.ViewHolder> {

    private List<String> items;
    private ImageView picMain;
    private Context context;
    private int selectedPosition = 0;

    public picListAdapter(ArrayList<String> items, ImageView pic) {
        this.items = items;
        this.picMain = pic;
    }

    @NonNull
    @Override
    public picListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPiclistBinding binding = ViewholderPiclistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull picListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(items.get(position))
                .into(holder.binding.pic);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == position) {
                    return; // Avoid reloading same image
                }

                int lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                Glide.with(context)
                        .load(items.get(position))
                        .into(picMain);
            }
        });

        if (selectedPosition == position) {
            holder.binding.picLayout.setBackgroundResource(R.drawable.white_bg);
        } else {
            holder.binding.picLayout.setBackgroundResource(R.drawable.grey_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPiclistBinding binding;

        public ViewHolder(ViewholderPiclistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
