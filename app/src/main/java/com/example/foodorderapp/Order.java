package com.example.foodorderapp;

public class Order {
    private int id;
    private String username;
    private String items;
    private double total;
    private long date;
    private String status;

    public Order() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}