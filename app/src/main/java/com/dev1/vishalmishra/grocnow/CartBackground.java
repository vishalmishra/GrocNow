package com.dev1.vishalmishra.grocnow;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class CartBackground extends AsyncTask<Void,CartItemStore,Void> {
    Context ctx1;
    Activity activity1;
    RecyclerView recyclerView1;
    RecyclerView.Adapter adapter1;
    RecyclerView.LayoutManager layoutManager;
 //   RealmResults<CartItemStore> arrayList=new ArrayList<>();
ProgressDialog progressDialog;
    public CartBackground(Context ctx){
        this.ctx1=ctx;
        activity1=(Activity)ctx;

    }

    @Override
    protected void onPreExecute() {
        recyclerView1=(RecyclerView)activity1.findViewById(R.id.recyclerview);
        layoutManager =new LinearLayoutManager(ctx1);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
   //     adapter1=new CartRecyclerAdapter(arrayList,ctx1);
        recyclerView1.setAdapter(adapter1);
        progressDialog = new ProgressDialog(ctx1,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Realm.init(ctx1);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItemStore> result = realm.where(CartItemStore.class).findAllAsync();
        result.load();
        int count=0;
    while(count<result.size())
           publishProgress(result.get(count));

progressDialog.dismiss();


        return null;
    }

    @Override
    protected void onProgressUpdate(CartItemStore... values) {
        //arrayList.add(values[0]);
        adapter1.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
    }
}
