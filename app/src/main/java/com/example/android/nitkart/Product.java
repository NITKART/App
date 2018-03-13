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

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("seller_name")
    private String seller_name;

    @SerializedName("seller_email")
    private String seller_email;

    @SerializedName("seller_block")
    private String seller_block;

    @SerializedName("seller_room")
    private String seller_room;

    @SerializedName("time_period")
    private String time_period;

    @SerializedName("seller_phone")
    private String seller_phone;

    @SerializedName("id")
    private String id;

    public Product(String image, String product_name, String product_price, String product_id, String seller_name, String seller_email, String seller_phone, String seller_block, String seller_room, String time_period, String id) {
        this.image = image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_id = product_id;
        this.seller_name = seller_name;
        this.seller_block = seller_block;
        this.seller_room = seller_room;
        this.time_period = time_period;
        this.id = id;
        this.seller_email = seller_email;
        this.seller_phone = seller_phone;
    }
    public String getImage() {
        return image;
    }
    public String getProduct_name() {
        return product_name;
    }
    public String getProduct_price() {
        return product_price;
    }
    public String getProduct_id(){
        return product_id;
    }
    public String getSeller_name(){
        return seller_name;
    }
    public String getSeller_block(){
        return seller_block;
    }
    public String getSeller_room(){
        return seller_room;
    }
    public String getTime_period(){
        return time_period;
    }
    public String getId(){
        return id;
    }
    public String getSeller_email(){
        return seller_email;
    }
    public String getSeller_phone(){
        return seller_phone;
    }
}
