package com.example.criptolist.Model;

import android.media.Image;
import android.widget.ImageView;

public class Cryptos {
    String ref;
    String name;
    String symbol;
    String price;
    String image;
    double amount;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Cryptos(String ref, String name, String symbol, String price, String image, double amount) {
        this.ref = ref;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }
    public double  ConvertDoubleString(String price){
        return  Double.parseDouble(price.replaceAll(",", "."));
    }
}
