package com.dev1.vishalmishra.grocnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class mycart extends AppCompatActivity {
    TextView cart_pname,amount;
    private Realm realm;
    Activity activity1;
    RecyclerView recyclerView1;
    Toolbar ptoolbar;
    ProgressDialog progressDialog;
    int Amount;
    //RecyclerView.Adapter adapter1;
    RecyclerView.LayoutManager layoutManager;
    RealmResults<CartItemStore> result;
    ArrayList<String> pname;
    String Pname;
    String url="http://grocnow.com/android/phone.php";
    CartRecyclerAdapter adapter;
    private RealmChangeListener realmChangeListener=new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            Log.d("abc","change occurs");
adapter.update(result);
Intent intent=getIntent();
finish();
startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);
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
        Realm realm = Realm.getDefaultInstance();
       result = realm.where(CartItemStore.class).findAllAsync();
        Iterator itr=result.iterator();
        int i=0;
       while (itr.hasNext()){
           itr.next();
           Amount+= (Integer.parseInt(result.get(i).getPrice())*Integer.parseInt(result.get(i).getPcount()));

           i++;

       }
        result.load();
        recyclerView1=(RecyclerView)findViewById(R.id.crecyclerview);
        layoutManager =new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        adapter=new CartRecyclerAdapter(result,this);
        recyclerView1.setAdapter(adapter);

        amount=(TextView)findViewById(R.id.amount);
        amount.setText(""+Amount);

        Button proceed=(Button)findViewById(R.id.proceed);

        Button start=(Button)findViewById(R.id.shop);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mycart.this,category.class);
                startActivity(intent);
            }
        });
        View view=findViewById(R.id.bottom);
        View view1=findViewById(R.id.empty);
        view1.setVisibility(view1.GONE);
        if(i==0){
            view1.setVisibility(view1.VISIBLE);
         view.setVisibility(view.GONE);
        }
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(mycart.this,
                        R.style.AppCompatAlertDialogStyle);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("login_success")) {
                                        Intent intent=new Intent(mycart.this,Continue_checkout.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("phone",jsonObject.getString("phone"));
                                        bundle.putString("address",jsonObject.getString("address"));
                                        intent.putExtras(bundle);
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                    }
                                    else{
                                        StyleableToast.makeText(getApplicationContext(), "Network error !", R.style.errortoast).show();
                                    }
                                } catch (JSONException e) {
                                    StyleableToast.makeText(getApplicationContext(), "Network error !", R.style.errortoast).show();
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(mycart.this, "Network error !", R.style.errortoast).show();
                        progressDialog.dismiss();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        SessionManager sessionManager=new SessionManager(mycart.this);
                        params.put("email", sessionManager.getEmail());
                        return params;
                    }
                };
                RequestQueue requestqueue = Volley.newRequestQueue(mycart.this);
                requestqueue.add(stringRequest);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        result.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        result.removeAllChangeListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
