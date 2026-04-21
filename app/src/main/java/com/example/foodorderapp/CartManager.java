package com.example.foodorderapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<MenuItemApi> cartItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addApiItem(MenuItemApi item) { cartItems.add(item); }
    public List<MenuItemApi> getItems() { return cartItems; }
    public void clear() { cartItems.clear(); }

    public double getTotal() {
        double total = 0;
        for (MenuItemApi item : cartItems) total += item.getPrice();
        return total;
    }

    public int getCount() { return cartItems.size(); }

    public String getItemsSummary() {
        StringBuilder sb = new StringBuilder();
        for (MenuItemApi item : cartItems) sb.append(item.getName()).append(", ");
        return sb.toString();
    }
}