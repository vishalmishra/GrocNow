package com.dev1.vishalmishra.grocnow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class product_list extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    TextView name, email;
    Button logout;
    SignInButton signInButton;
    ImageView imageView;
    GoogleApiClient googleApiClient;
    static final int Req_code = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        name = (TextView) findViewById(R.id.gname);
        email = (TextView) findViewById(R.id.gmail);
        logout = (Button) findViewById(R.id.logout);
        imageView = (ImageView) findViewById(R.id.product_image);
        signInButton = (SignInButton) findViewById(R.id.btg_login);


        logout.setOnClickListener(this);
        signInButton.setOnClickListener(this);


    /*    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btg_login:
                signout();
                break;
            case R.id.logout:
                signin();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signin() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, Req_code);

    }

    private void signout() {
Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
    @Override
    public void onResult(@NonNull Status status) {
        updateui(false);
    }
});

    }

    private void handleresult(GoogleSignInResult result) {
if(result.isSuccess()){
    GoogleSignInAccount acct=result.getSignInAccount();
    String personName = acct.getDisplayName();
    String personPhotoUrl = acct.getPhotoUrl().toString();
    String email = acct.getEmail();

    name.setText(personName);
    this.email.setText(email);
    Glide.with(this).load(personPhotoUrl).into(imageView);
    updateui(true);
}
else{
    updateui(false);
}
    }

    private void updateui(boolean islogin) {

if(!islogin){
    Log.e("error","sign in error....");
}
    }
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
       if (requestCode == Req_code) {
           GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           handleresult(result);
       }
   }
}


