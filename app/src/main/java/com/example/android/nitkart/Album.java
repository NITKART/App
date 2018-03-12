package com.example.android.nitkart;

import java.io.Serializable;

public class Album implements Serializable {
    private String name;
    private String price;
    private String url;

    public Album() {
    }

    public Album(String name, String price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

}
