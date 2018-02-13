package com.dev1.vishalmishra.grocnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class Continue_checkout extends AppCompatActivity {
Toolbar ptoolbar;
ProgressDialog progressDialog;
Button confirm;
int count=0;
    @BindView(R.id.chphone) EditText phone;
    @BindView(R.id.chaddress) EditText address;
String url="http://grocnow.com/android/orderstable.php";
    private Realm realm;
String pid,pname,price,quantity;
    RealmResults<CartItemStore> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_checkout);
        ButterKnife.bind(this);
        ptoolbar=(Toolbar)findViewById(R.id.ctoolbar);
        setSupportActionBar(ptoolbar);
        ptoolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        ptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        Bundle bundle=getIntent().getExtras();
        if(bundle.getString("phone")!="")
        phone.setText(bundle.getString("phone"));
        if(!bundle.getString("address").equals("Not Available"))
        address.setText(bundle.getString("address"));
        confirm=(Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setEnabled(false);
                progressDialog = new ProgressDialog(Continue_checkout.this,R.style.AppCompatAlertDialogStyle);
                progressDialog.setTitle("Placing the order");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                final String Phone=phone.getText().toString();
                final String Address=address.getText().toString();
                results = realm.where(CartItemStore.class).findAll();
                results.load();
                int i=0;

              Iterator itr=results.iterator();
              while(itr.hasNext()) {
                  itr.next();
                 pid=results.get(i).getPid();
                  pname=results.get(i).getPname();
                  price=results.get(i).getPrice();
                  quantity=results.get(i).getPcount();
                  i++;
                  Log.v("value",""+i);
                  try {
                      Thread.sleep(200);
                      Volley(pid,pname,price,quantity,Phone,Address);
                      Log.v("count",""+count);
                      Thread.sleep(200);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                 // Volley(pid,pname,price,quantity,Phone,Address);


              }
               if(count==i){
                   try {
                       Thread.sleep(300);
                       progressDialog.dismiss();
                       SweetAlertDialog pDialog = new SweetAlertDialog(Continue_checkout.this, SweetAlertDialog.SUCCESS_TYPE);
                       pDialog.setTitleText("Thank you!")
                               .setContentText("Your order will be shpped soon..").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               Intent intent=new Intent(Continue_checkout.this,MainActivity.class);
                               startActivity(intent);
                               finish();
                           }
                       })
                               .show();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }


              }
            }
        });
    }

    private int Volley(final String pid, final String pname, final String price, final String quantity, final String Phone, final String Address) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("order_success")) {

                            }
                            else{
                                StyleableToast.makeText(getApplicationContext(), "Order failed", R.style.errortoast).show();
                            }
                        } catch (JSONException e) {
                            StyleableToast.makeText(getApplicationContext(), "Network error !", R.style.errortoast).show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(Continue_checkout.this, "Network error !", R.style.errortoast).show();
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager=new SessionManager(Continue_checkout.this);
                params.put("pid", pid);
                params.put("pname", pname);
                params.put("price", price);
                params.put("quantity", quantity);
                params.put("amount", "200");
                params.put("name", sessionManager.getname());
                params.put("email", sessionManager.getEmail());
                params.put("phone", Phone);
                params.put("address", Address);
                params.put("status", "Undelivered");
                return params;
            }
        };
       // MySingleton.getInstances(Continue_checkout.this).addToRequestQue(stringRequest);
        RequestQueue requestqueue = Volley.newRequestQueue(Continue_checkout.this);
        requestqueue.add(stringRequest);
        return 1;
    }
}
