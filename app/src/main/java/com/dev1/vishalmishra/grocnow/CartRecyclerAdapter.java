package com.dev1.vishalmishra.grocnow;


import android.content.Context;
import android.content.Intent;
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
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class CartRecyclerAdapter extends RecyclerView.Adapter <CartRecyclerAdapter.RecyclerViewHolder>{
    private static final int TYPE_HEAD=0;
    private static final int TYPE_LIST=1;
    private Context context;
private Realm realm;

   RealmResults<CartItemStore> arrayList;

    public CartRecyclerAdapter(RealmResults<CartItemStore> arrayList, Context context){
        this.context = context;
        update(arrayList);
    }
    public void update(RealmResults<CartItemStore> Results){
        this.arrayList=Results;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final CartItemStore products=arrayList.get(position);
        realm = Realm.getDefaultInstance();
        //   holder.pid.setText(products.getPid());
        holder.pname.setText(products.getPname());
        holder.price.setText(""+Integer.parseInt(products.getPrice())*Integer.parseInt(products.getPcount()));
//holder.imurl.setText(products.getImurl());
//holder.category.setText(products.getCategory());
        holder.pcount.setText(products.getPcount());
        String img="http://grocnow.com/images/products/"+products.getPid()+".jpg";
        Glide.with(context).load(img).into(holder.imageView);
        holder.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmResults<CartItemStore> results = realm.where(CartItemStore.class).findAll();
                realm.beginTransaction();
                int a=Integer.parseInt(holder.pcount.getText().toString());
                    holder.pcount.setText(""+(a+1));
                    holder.price.setText(""+(Integer.parseInt(holder.price.getText().toString())+Integer.parseInt(products.getPrice())));
                    results.get(position).setPcount(""+(a+1));
                realm.commitTransaction();
            }
        });
        holder.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmResults<CartItemStore> results = realm.where(CartItemStore.class).findAll();
                realm.beginTransaction();
                int a=Integer.parseInt(holder.pcount.getText().toString());
                if(a>1){
                    holder.pcount.setText(""+(a-1));
                    holder.price.setText(""+(Integer.parseInt(holder.price.getText().toString())-Integer.parseInt(products.getPrice())));
                    results.get(position).setPcount(""+(a-1));
                }
                realm.commitTransaction();
            }
        });
holder.pdelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Remove item from the cart!")
                .setConfirmText("Yes")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        RealmResults<CartItemStore> results = realm.where(CartItemStore.class).findAll();
                        realm.beginTransaction();
                        // remove single match
                        results.deleteFromRealm(position);
                        realm.commitTransaction();


                    }
                })
                .show();

    }
});

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView pid,pname,price,imurl,category,pcount;ImageView imageView;
        Button inc,dec;ImageButton pdelete;
        public RecyclerViewHolder(View view){
            super(view);
            //  pid=(TextView)view.findViewById(R.id.pid);
            pname=(TextView)view.findViewById(R.id.pname);
            price=(TextView)view.findViewById(R.id.price);
            // imurl=(TextView)view.findViewById(R.id..imurl);
            // category=(TextView)view.findViewById(R.id.category);
            imageView=(ImageView)view.findViewById(R.id.product_image);
            inc=(Button)view.findViewById(R.id.inc);
            dec=(Button)view.findViewById(R.id.dec);
            pcount=(TextView) view.findViewById(R.id.pcount);
            pdelete=(ImageButton)view.findViewById(R.id.pdelete);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
