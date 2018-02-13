package com.dev1.vishalmishra.grocnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Login extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.googlebutton) Button googlebutton;
    ProgressDialog progressDialog;
    GoogleApiClient googleApiClient;
    String personName,personPhotoUrl,gmail;
    static final int Req_code = 9001;
    String log_url="http://grocnow.com/android/glogin.php";
    String google_log="http://grocnow.com/android/googlelogin.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlelogin();
            }
        });
        _signupLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        progressDialog = new ProgressDialog(Login.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        //login auth
        StringRequest stringRequest=new StringRequest(Request.Method.POST, log_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            String code=jsonObject.getString("code");
                            if(code.equals("login_failed")){
                                _loginButton.setEnabled(true);
                                progressDialog.dismiss();
                               // Toast.makeText(Login.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                               // Toasty.error(getApplicationContext(),"Invalid email or password",Toast.LENGTH_SHORT,true).show();
                                StyleableToast.makeText(getApplicationContext(), "Invalid email or password", R.style.errortoast).show();
                                _passwordText.setText("");
                            }
                            else {

                                Intent intent=new Intent(Login.this,MainActivity.class);
                              //  Bundle bundle=new Bundle();
                                String name=jsonObject.getString("name");
                               /* bundle.putString("name",jsonObject.getString("name"));
                                bundle.putString("email",jsonObject.getString("email"));
                                bundle.putString("phone",jsonObject.getString("phone"));*/
                                try{
                                    DB snappyDB= DBFactory.open(Login.this);
                                    snappyDB.put("name",jsonObject.getString("name").trim());
                                    snappyDB.put("email",jsonObject.getString("email").trim());
                                    snappyDB.put("phone",jsonObject.getString("phone").trim());
                                    snappyDB.close();
                                }
                                catch (SnappydbException e) {
                                    e.printStackTrace();}
                               // Toasty.success(getApplicationContext(),"Welcome "+name,Toast.LENGTH_SHORT).show();
                                StyleableToast.makeText(getApplicationContext(),"Welcome "+name, R.style.mytoast).show();
                                //intent.putExtras(bundle);
                               // intent2.putExtras(bundle);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(Login.this,"Network error",R.style.errortoast).show();
                progressDialog.dismiss();
                _loginButton.setEnabled(true);
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        MySingleton.getInstances(Login.this).addToRequestQue(stringRequest);

      /*  new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

   /* public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }*/

    public void onLoginFailed() {
       // Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 14) {
            _passwordText.setError("between 6 and 14 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private void googlelogin() {
        progressDialog = new ProgressDialog(Login.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, Req_code);


        //login auth
       }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void handleresult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount acct=result.getSignInAccount();
           personName = acct.getDisplayName();
try{           personPhotoUrl = acct.getPhotoUrl().toString();}
catch (Exception e){

}
            gmail = acct.getEmail();
gvolleylogin();

        }
        else{

            Log.e("error", "log in error");
        }
    }

    private void updateui(boolean islogin) {

        if(!islogin){
            Log.e("error","sign in error....");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Req_code) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);
        }

       else if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }   }
protected  void gvolleylogin(){

        Log.v("volley",personName+gmail);

    StringRequest stringRequest2=new StringRequest(Request.Method.POST, google_log,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("response",response);
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String code=jsonObject.getString("code");
                        if(code.equals("reg_success")){
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            DB snappydb =null;
                            try{
                                DB snappyDB= DBFactory.open(Login.this);
                                snappyDB.put("name",personName);
                                snappyDB.put("email",gmail);
                                snappyDB.put("phone","");
                                snappyDB.put("imgurl",personPhotoUrl);
                                snappyDB.close();
                            }
                            catch (SnappydbException e) {
                                e.printStackTrace();}
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                            StyleableToast.makeText(getApplicationContext(),"Welcome "+ personName, R.style.mytoast).show();
                        }
                        if(code.equals("login_success")) {
                            progressDialog.dismiss();
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            String name=jsonObject.getString("name");

                            try{
                                DB snappyDB= DBFactory.open(Login.this);
                                snappyDB.put("name",jsonObject.getString("name").trim());
                                snappyDB.put("email",jsonObject.getString("email").trim());
                                snappyDB.put("phone",jsonObject.getString("phone").trim());
                                snappyDB.put("imgurl",personPhotoUrl);
                                snappyDB.close();
                            }
                            catch (SnappydbException e) {
                                e.printStackTrace();}
                            StyleableToast.makeText(getApplicationContext(),"Welcome "+name, R.style.mytoast).show();

                            startActivity(intent);
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            StyleableToast.makeText(getApplicationContext(), "Network Connection Problem", R.style.errortoast).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            StyleableToast.makeText(Login.this,"Network error",R.style.errortoast).show();
                progressDialog.dismiss();
            error.printStackTrace();
        }
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params= new HashMap<String, String>();
            params.put("name", personName);
            params.put("email", gmail);
            params.put("phone", "");
            params.put("password", "");
            params.put("address", "Not Available");
            params.put("points","0");
            return params;
        }
    };
    MySingleton.getInstances(Login.this).addToRequestQue(stringRequest2);

}
    public void signout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });

    }
}


