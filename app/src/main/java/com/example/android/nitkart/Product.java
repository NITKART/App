package com.example.android.nitkart;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suyash on 10-03-2018.
 */

public class Product {
    @SerializedName("image")
    private String image;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_price")
    private String product_price;
    public Product(String image, String product_name, String product_price) {
        this.image = image;
        this.product_name = product_name;
        this.product_price = product_price;
    }
    public String getImage() {
        return image;
    }
    public String getName() {
        return product_name;
    }
    public String getPrice() {
        return product_price;
    }
}
