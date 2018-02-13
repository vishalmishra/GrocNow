package com.dev1.vishalmishra.grocnow;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class Launcher extends AppCompatActivity {
    DB snappyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ShimmerTextView textView = (ShimmerTextView) findViewById(R.id.shimmertv);
                    Shimmer shimmer = new Shimmer();
                    shimmer.start(textView);
                    Thread.sleep(2000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    SessionManager sessionManager=new SessionManager(Launcher.this);
                    if (sessionManager.isLoggedIn()){
                        startActivity(new Intent(Launcher.this, MainActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(Launcher.this, Login.class));
                        finish();
                    }
                }
            }
        });
        thread.start();

    }


}
