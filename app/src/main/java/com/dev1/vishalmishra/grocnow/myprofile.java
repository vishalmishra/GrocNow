package com.dev1.vishalmishra.grocnow;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class myprofile extends AppCompatActivity {
    @BindView(R.id.pname) TextView name;
    @BindView(R.id.pemail) TextView email;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.profile_photo) ImageView profile_photo;
     Toolbar ptoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        ptoolbar=(Toolbar)findViewById(R.id.ptoolbar);
        setSupportActionBar(ptoolbar);
        ptoolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        ptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       final ProgressDialog progressDialog = new ProgressDialog(myprofile.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        SessionManager sessionManager=new SessionManager(myprofile.this);
                        name.setText(sessionManager.getname());
                        email.setText(sessionManager.getEmail());
                        phone.setText(sessionManager.getphone());
                        Glide.with(myprofile.this).load(sessionManager.getImgurl()).placeholder(R.drawable.pro_place).into(profile_photo);
                        progressDialog.dismiss();
                    }
                }, 1000);


    }

}
