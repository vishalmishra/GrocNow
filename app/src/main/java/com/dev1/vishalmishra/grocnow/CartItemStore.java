package com.dev1.vishalmishra.grocnow;

import android.content.Context;



import java.util.ArrayList;

import io.realm.RealmObject;


public class CartItemStore extends RealmObject {

   private String pname;
   private String price;
   private String pid;
    private String pcount;

   /* @Override
    public String toString() {
        return "CartItemStore{" +
                "pname='" + pname + '\'' +
                ", price='" + price + '\'' +
                ", imurl='" + imurl + '\'' +
                ", pcount='" + pcount + '\'' +
                '}';
    }*/


    public String getPcount() {
        return pcount;
    }

    public void setPcount(String pcount) {
        this.pcount = pcount;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}



