package com.dev1.vishalmishra.grocnow;


import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class SessionManager {
    String name,email,phone,imgurl;
    Context context;
    Boolean access;
    public SessionManager(Context context) {
        this.context=context;
        email=null;
        name=null;
        phone=null;
        imgurl=null;
        access=false;
    }
    public String getImgurl() {
        try
        {
            DB snappyDB= DBFactory.open(context);
            if (snappyDB.exists("imgurl"))
               imgurl=snappyDB.get("imgurl");
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return imgurl;
    }
    public String getphone() {
        try
        {
            DB snappyDB= DBFactory.open(context);
            if (snappyDB.exists("phone"))
                phone=snappyDB.get("phone");
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return phone;
    }
    public String getname() {
        try
        {
            DB snappyDB= DBFactory.open(context);
            if (snappyDB.exists("name"))
                name=snappyDB.get("name");
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return name;
    }


    public String getEmail() {
        try
        {
            DB snappyDB= DBFactory.open(context);
            if (snappyDB.exists("email"))
                email=snappyDB.get("email");
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return email;
    }


    public Boolean getAccess()
    {
        try
        {
            DB snappyDB= DBFactory.open(context);
            if (snappyDB.exists("access")) {
                access = snappyDB.getBoolean("access");
                return access;
            }
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean isLoggedIn()
    {
        try
        {
            DB snappyDB= DBFactory.open(context);
          //  if (snappyDB.exists("email") && snappyDB.exists("name") && snappyDB.exists("phone"))
                if (snappyDB.exists("email")){
                snappyDB.close();
                return true;
            }
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void clear()
    {
        try
        {
            DB snappyDB= DBFactory.open(context);
            snappyDB.destroy();
            snappyDB.close();
        }
        catch (SnappydbException e)
        {
            e.printStackTrace();
        }
    }
}