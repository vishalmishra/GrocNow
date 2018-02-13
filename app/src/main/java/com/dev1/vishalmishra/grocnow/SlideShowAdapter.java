package com.dev1.vishalmishra.grocnow;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;



public class SlideShowAdapter extends PagerAdapter {

    private Context context;
    LayoutInflater inflater;

    public String[] images = {
            "http://grocnow.com/ba1.jpg",
            "http://grocnow.com/ba.jpg",
            "http://grocnow.com/ba2.jpg"
    };


    public SlideShowAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {

        return images.length;

    }


    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view==(LinearLayout)object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.slideshow_layout,container,false);

        ImageView img = (ImageView) view.findViewById(R.id.imageView_id);

        //img.setImageResource(images[position]);
       Glide.with(context).load(images[position]).into(img);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout)object);

    }


}
