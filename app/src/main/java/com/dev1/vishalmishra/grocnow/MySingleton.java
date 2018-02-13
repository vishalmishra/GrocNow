package com.dev1.vishalmishra.grocnow;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    public static MySingleton mInstances;
    private RequestQueue requestQueue;
    private static Context mctx;

    private MySingleton(Context context){
    mctx = context;
    requestQueue= getrequestQueue();

  }
    public RequestQueue getrequestQueue(){

        if (requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getInstances(Context context){
        if(mInstances==null){
          mInstances= new MySingleton(context);
        }
        return mInstances;
    }
     public <T> void addToRequestQue(Request<T> request){
         requestQueue.add(request);
     }
}
