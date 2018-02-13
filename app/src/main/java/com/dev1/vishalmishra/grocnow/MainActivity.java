package com.dev1.vishalmishra.grocnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
Toolbar toolbar;

DrawerLayout drawerLayout;
NavigationView navigationView;
Timer timer;
SlideShowAdapter slideShowAdapter;
TextView textCartItemCount;
Realm realm;
    GoogleApiClient mGoogleApiClient;
final SessionManager sessionManager=new SessionManager(MainActivity.this);
@BindView(R.id.bt_category) Button bt_category;
   @BindView(R.id.viewPager) ViewPager viewPager;
   @BindView(R.id.search) Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.circleIndicator);
        slideShowAdapter=new SlideShowAdapter(this);
        viewPager.setAdapter(slideShowAdapter);
        indicator.setViewPager(viewPager);



        final Handler handler=new Handler();

        final Runnable runnable = new Runnable(){
            @Override
            public void run() {
int i=viewPager.getCurrentItem();
if(i==slideShowAdapter.images.length-1){
    i=0;
    viewPager.setCurrentItem(i,true);
}
else{
    i++;
    viewPager.setCurrentItem(i,true);
}
            }
        };
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },4000,4000);

        bt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,category.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,search.class);
                startActivity(intent);
            }
        });
      /*  Bundle bundle=getIntent().getExtras();

            name.setText(bundle.getString("name"));
            email.setText(bundle.getString("email"));
           phone.setText(bundle.getString("phone"));
*/
drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
navigationView=(NavigationView)findViewById(R.id.navigation_view);
navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
   drawerLayout.setDrawerListener(toggle);
   toggle.syncState();
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
            Toast.makeText(MainActivity.this,"Notification",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.my_profile:

         Intent intent=new Intent(getApplicationContext(),myprofile.class);
          startActivity(intent);
                break;
            case R.id.my_cart:
                Intent intent2=new Intent(getApplicationContext(),mycart.class);
                startActivity(intent2);
                Toast.makeText(getApplicationContext(),"my cart",Toast.LENGTH_SHORT).show();
            break;
            case R.id.logout:
                sessionManager.clear();
                RealmResults<CartItemStore> results = realm.where(CartItemStore.class).findAll();
                realm.beginTransaction();
                results.deleteAllFromRealm();
                realm.commitTransaction();
                logout();
                            Intent intent3=new Intent(MainActivity.this,Login.class);
                startActivity(intent3);

                finish();
                break;
            case R.id.my_order:
                Intent intent4=new Intent(getApplicationContext(),myorders.class);
                startActivity(intent4);
break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }

    });}

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {finish();
            }
    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
}
