package com.dev1.vishalmishra.grocnow;

import android.content.Intent;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;


public class test extends AppCompatActivity {
TextView textView;
ImageView imageView;
String category;
Toolbar ptoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        ptoolbar=(Toolbar)findViewById(R.id.protoolbar);
        setSupportActionBar(ptoolbar);
        ptoolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        ptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle=getIntent().getExtras();

        category=bundle.getString("cat");
      background background=new background(test.this,category);
      background.execute();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.cart)
        {
            Intent intent=new Intent(getApplicationContext(),mycart.class);
            startActivity(intent);
        }
        if(id==R.id.notify){
            Toast.makeText(test.this,"Notification",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
