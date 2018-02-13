package com.dev1.vishalmishra.grocnow;


import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;



public class OrderRecyclerAdapter extends RecyclerView.Adapter <OrderRecyclerAdapter.RecyclerViewHolder>{
    private static final int TYPE_HEAD=0;
    private static final int TYPE_LIST=1;
    private Context context;

    private Realm realm;
    ArrayList<myorders_list> arrayList=new ArrayList<>();

    public OrderRecyclerAdapter(ArrayList<myorders_list> arrayList, Context context){
        this.arrayList=arrayList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_row,parent,false);
        RecyclerViewHolder recycleViewHolder=new RecyclerViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final myorders_list products=arrayList.get(position);

        holder.pname.setText(products.getPname());
        holder.price.setText(products.getPrice());
        String img="http://grocnow.com/images/products/"+products.getPid()+".jpg";
        Glide.with(context).load(img).into(holder.imageView);

       holder.quantity.setText(products.getQuantity());
       holder.status.setText(products.getStatus());
       holder.time.setText(products.getTime());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView pid,pname,price,status,time,quantity;ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);
            //  pid=(TextView)view.findViewById(R.id.pid);
            pname=(TextView)view.findViewById(R.id.pname);
            price=(TextView)view.findViewById(R.id.price);
             status=(TextView)view.findViewById(R.id.status);
            quantity=(TextView)view.findViewById(R.id.pcount);
            imageView=(ImageView)view.findViewById(R.id.product_image);
            time=(TextView) view.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
