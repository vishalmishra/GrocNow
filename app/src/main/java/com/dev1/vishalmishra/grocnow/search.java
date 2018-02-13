package com.dev1.vishalmishra.grocnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class search extends AppCompatActivity {

    MaterialSearchView searchView;
    String product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText editText=(EditText)findViewById(R.id.search);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchbackground searchbackground=new searchbackground(search.this,editText.getText().toString());
                    searchbackground.execute();
                    return true;
                }
                return false;
            }
        });
        ImageButton imageButton=(ImageButton)findViewById(R.id.bt_search);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchbackground searchbackground=new searchbackground(search.this,editText.getText().toString());
                searchbackground.execute();
            }
        });
       }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
