package com.dev1.vishalmishra.grocnow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
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

public class Signup extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;


    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String reg_url="http://grocnow.com/android/gregister.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _loginLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        builder=new AlertDialog.Builder(Signup.this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();


            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

       progressDialog = new ProgressDialog(Signup.this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String phone = contact.getText().toString();
        final String password = _passwordText.getText().toString();
        final String address="Not Available";
final String points="0";

        // TODO: Implement your own signup logic here.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                           // String message = jsonObject.getString("message");
                           // builder.setTitle("Server Response..");
                           // builder.setMessage(message);
                            onSignupSuccess(code);
                        } catch (JSONException e) {
                            StyleableToast.makeText(getApplicationContext(), "Network error !", R.style.errortoast).show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(Signup.this,"Network error !",R.style.errortoast).show();
                progressDialog.dismiss();
                _signupButton.setEnabled(true);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("address", address);
                params.put("points", points);
                return params;
            }
        };
        MySingleton.getInstances(Signup.this).addToRequestQue(stringRequest);

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                 //       onSignupSuccess(code);

                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess(final String code) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        if(code.equals("reg_success")){
            progressDialog.dismiss();
            Intent intent=new Intent(Signup.this,MainActivity.class);
            DB snappydb =null;
            try{
                DB snappyDB= DBFactory.open(Signup.this);
                snappyDB.put("name",_nameText.getText().toString());
                snappyDB.put("email",_emailText.getText().toString());
                snappyDB.put("phone",contact.getText().toString());
                snappyDB.close();
            }
            catch (SnappydbException e) {
                e.printStackTrace();}
            startActivity(intent);
            finish();
            //Toast.makeText(getApplicationContext(),"Welcome "+_nameText.getText().toString(),Toast.LENGTH_LONG).show();
            StyleableToast.makeText(getApplicationContext(),"Welcome "+ _nameText.getText().toString(), R.style.mytoast).show();

        }
        else if(code.equals("reg_failed")){
            _emailText.setText("");
            _passwordText.setText("");
            progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(),"Signup failed",Toast.LENGTH_LONG).show();
            StyleableToast.makeText(getApplicationContext(), "User Already exist", R.style.errortoast).show();


        }

    }

    public void onSignupFailed() {
       // Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = contact.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (phone.isEmpty() || phone.length() !=10) {
            contact.setError("enter a valid phone no.");
            valid = false;
        } else {
            contact.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 14) {
            _passwordText.setError("between 6 and 14 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
   /* public void displayAlert(final  String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error"))
                {
                    _passwordText.setText("");
                }
               else if(code.equals("reg_success")){
                    finish();
                }
                else if(code.equals("reg_failed")){

                    _passwordText.setText("");

                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }*/
}

