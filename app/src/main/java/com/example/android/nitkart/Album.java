package com.example.android.nitkart;

import java.io.Serializable;

public class Album implements Serializable {
    private String product_name;
    private String product_price;
    private String url;
    private String product_id;
    private String seller_name;
    private String seller_phone;
    private String seller_email;
    private String seller_block;
    private String seller_room;
    private String time_period;

    public Album() {
    }

    public Album(String product_name, String product_price, String url, String product_id, String seller_name, String seller_phone, String seller_email, String seller_block, String seller_room, String time_period) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.url = url;
        this.product_id = product_id;
        this.seller_name = seller_name;
        this.seller_phone = seller_phone;
        this.seller_email = seller_email;
        this.seller_block = seller_block;
        this.seller_room = seller_room;
        this.time_period = time_period;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getUrl() {
        return url;
    }

    public String getProduct_id() {
        return  product_id;
    }

    public String getSeller_name(){
        return seller_name;
    }

    public String getSeller_phone()
    {
        return seller_phone;
    }

    public String getSeller_email()
    {
        return seller_email;
    }

    public String getSeller_block()
    {
        return seller_block;
    }

    public String getSeller_room()
    {
        return seller_room;
    }

    public String getTime_period()
    {
        return time_period;
    }

}
