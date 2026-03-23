package com.example.onlineshopstoreapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.onlineshopstoreapp.Domain.ItemsModel;

import java.util.ArrayList;

public class ManagmentFavorite {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentFavorite(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItem(ItemsModel item) {
        ArrayList<ItemsModel> listItem = getListFavorite();
        boolean existAlready = false;
        for (int y = 0; y < listItem.size(); y++) {
            if (listItem.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                break;
            }
        }
        if (existAlready) {
            Toast.makeText(context, "Already in your Favorites", Toast.LENGTH_SHORT).show();
        } else {
            listItem.add(item);
            tinyDB.putListObject("FavoriteList", listItem);
            Toast.makeText(context, "Added to your Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ItemsModel> getListFavorite() {
        return tinyDB.getListObject("FavoriteList");
    }

    public void removeItem(ItemsModel item) {
        ArrayList<ItemsModel> listItem = getListFavorite();
        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getTitle().equals(item.getTitle())) {
                listItem.remove(i);
                break;
            }
        }
        tinyDB.putListObject("FavoriteList", listItem);
        Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    public boolean isFavorite(ItemsModel item) {
        ArrayList<ItemsModel> listItem = getListFavorite();
        for (ItemsModel favoriteItem : listItem) {
            if (favoriteItem.getTitle().equals(item.getTitle())) {
                return true;
            }
        }
        return false;
    }
}
