package com.dev1.vishalmishra.grocnow;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myordersbackground extends AsyncTask<Void,myorders_list,Void> {
    Context ctx;
    Activity activity;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<myorders_list> arrayList=new ArrayList<>();
    String category;
    ProgressDialog progressDialog;
    public myordersbackground(Context ctx){
        this.ctx=ctx;
        activity=(Activity)ctx;

    }
    String string="http://grocnow.com/android/orders.php";


    @Override
    protected void onPreExecute() {
        recyclerView=(RecyclerView)activity.findViewById(R.id.myrecyclerview);
        layoutManager =new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new OrderRecyclerAdapter(arrayList,ctx);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(ctx,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, string,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("server_response");
                            int count=0;
                            while(count<jsonArray.length()){

                                JSONObject JO=jsonArray.getJSONObject(count);
                                count++;
                                myorders_list products=new myorders_list(JO.getString("pid"),JO.getString("pname"),JO.getString("price"),JO.getString("quantity"),JO.getString("status"),JO.getString("time"));
                                publishProgress(products);

                            }
                            Log.d("JSON STRING",response);
progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(ctx,"Network error",R.style.errortoast).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager=new SessionManager(ctx);
                Map<String,String> params= new HashMap<String, String>();
                params.put("email",sessionManager.getEmail());
                return params;
            }
        };
        MySingleton.getInstances(ctx).addToRequestQue(stringRequest);


        return null;
    }
    @Override
    protected void onProgressUpdate(myorders_list... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);

    }
}
