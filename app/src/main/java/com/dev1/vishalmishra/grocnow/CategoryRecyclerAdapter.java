package com.dev1.vishalmishra.grocnow;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;



public class CategoryRecyclerAdapter extends RecyclerView.Adapter <CategoryRecyclerAdapter.CRecyclerViewHolder>{
    private static final int TYPE_HEAD=0;
    private static final int TYPE_LIST=1;
    private Context context;


    ArrayList<category_set> arrayList=new ArrayList<>();

    public CategoryRecyclerAdapter(ArrayList<category_set> arrayList, Context context){
        this.arrayList=arrayList;
        this.context = context;
    }

    @Override
    public CRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout_row,parent,false);
        CRecyclerViewHolder recycleViewHolder=new CRecyclerViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(final CRecyclerViewHolder holder, int position) {
        final category_set category_set=arrayList.get(position);

        //   holder.pid.setText(products.getPid());
        holder.cname.setText(category_set.getCname());
String img="http://grocnow.com/images/category/"+category_set.getCid()+".jpg";
//holder.imurl.setText(products.getImurl());
//holder.category.setText(products.getCategory());
        Glide.with(context).load(img).into(holder.imageView);

    holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent=new Intent(context,test.class);
            Bundle bundle=new Bundle();
          bundle.putString("cat",category_set.getCname());
             intent.putExtras(bundle);
            context.startActivity(intent);
        }
    });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class CRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView cname,pcount;ImageView imageView;
RelativeLayout relativeLayout;
        public CRecyclerViewHolder(View view){
            super(view);
           //cid=(TextView)view.findViewById(R.id.pid);
            cname=(TextView)view.findViewById(R.id.cname);
           // price=(TextView)view.findViewById(R.id.price);
            // imurl=(TextView)view.findViewById(R.id..imurl);
            // category=(TextView)view.findViewById(R.id.category);
            imageView=(ImageView)view.findViewById(R.id.cimg);
relativeLayout=(RelativeLayout)view.findViewById(R.id.rel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
