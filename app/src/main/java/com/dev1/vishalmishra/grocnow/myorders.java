package com.dev1.vishalmishra.grocnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class myorders extends AppCompatActivity {
Toolbar ptoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        ptoolbar=(Toolbar)findViewById(R.id.protoolbar);
        setSupportActionBar(ptoolbar);
        ptoolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        ptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        myordersbackground background=new myordersbackground(myorders.this);
        background.execute();

    }

}
