package com.dev1.vishalmishra.grocnow;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class CategoryBackground extends AsyncTask<Void,category_set,Void> {
    Context ctx;
    Activity activity;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<category_set> arrayList=new ArrayList<>();
ProgressDialog progressDialog;
    public CategoryBackground(Context ctx){
        this.ctx=ctx;
        activity=(Activity)ctx;

    }
    String string="http://grocnow.com/android/category.php";


    @Override
    protected void onPreExecute() {
        recyclerView=(RecyclerView)activity.findViewById(R.id.catrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
       // layoutManager =new LinearLayoutManager(ctx);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new CategoryRecyclerAdapter(arrayList,ctx);
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
        try {
            URL url= new URL(string);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            String line;

            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }

            httpURLConnection.disconnect();

            String json=stringBuilder.toString().trim();
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("server_response");
            int count=0;
            while(count<jsonArray.length()){

                JSONObject JO=jsonArray.getJSONObject(count);
                count++;
                category_set category_set=new category_set(JO.getString("cid"),JO.getString("cname"));
                publishProgress(category_set);
            }
            Log.d("JSON2 STRING",json);
progressDialog.dismiss();

        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(category_set... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);

    }
}
