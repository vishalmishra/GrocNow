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



public class RecycleViewAdapter extends RecyclerView.Adapter <RecycleViewAdapter.RecycleViewHolder>{
private static final int TYPE_HEAD=0;
private static final int TYPE_LIST=1;
    private Context context;

private Realm realm;
    ArrayList<products> arrayList=new ArrayList<>();

    public RecycleViewAdapter(ArrayList<products> arrayList, Context context){
        this.arrayList=arrayList;
        this.context = context;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        RecycleViewHolder recycleViewHolder=new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, int position) {
  final products products=arrayList.get(position);
        realm = Realm.getDefaultInstance();
     //   holder.pid.setText(products.getPid());


  holder.pname.setText(products.getPname());
holder.price.setText(products.getPrice());
//holder.imurl.setText(products.getImurl());
//holder.category.setText(products.getCategory());
        String img="http://grocnow.com/images/products/"+products.getPid()+".jpg";
        Glide.with(context).load(img).into(holder.imageView);
        holder.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.pcount.setText(""+(Integer.parseInt(holder.pcount.getText().toString())+1));
                holder.price.setText(""+(Integer.parseInt(holder.price.getText().toString())+Integer.parseInt(products.getPrice())));

            }
        });
        holder.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=Integer.parseInt(holder.pcount.getText().toString());
                if(a>1){
                holder.pcount.setText(""+(a-1));
                    holder.price.setText(""+(Integer.parseInt(holder.price.getText().toString())-Integer.parseInt(products.getPrice())));

                }
            }
        });

        holder.pcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           SavetoDatabase(products.getPid(),products.getPname().trim(),products.getPrice(),holder.pcount.getText().toString().trim());

            }
        });
    }

    private void SavetoDatabase(final String pid,final String pname,final String price,final String pcount) {
       final RealmResults<CartItemStore> results = realm.where(CartItemStore.class).equalTo("pname",pname).findAll();
       final int a=results.size();
       SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        //pDialog.setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Thank you!")
                .setContentText("Item added to cart")
                .show();
       realm.beginTransaction();
        if(a!=0) {
            int con=Integer.parseInt(results.get(0).getPcount())+Integer.parseInt(pcount);
           results.get(0).setPcount(""+con);
        }
             else{   CartItemStore cartItemStore = realm.createObject(CartItemStore.class);
            cartItemStore.setPid(pid);
                cartItemStore.setPname(pname);
                cartItemStore.setPrice(price);
                cartItemStore.setPcount(pcount);
        }
realm.commitTransaction();

realm.close();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView pid,pname,price,imurl,category,pcount;ImageView imageView;
        Button inc,dec;Button pcart;
        public RecycleViewHolder(View view){
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
            pcart=(Button)view.findViewById(R.id.pcart);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
