package com.baidumap.innerac.baidumaptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        enterMap();
    }

    private void enterMap(){
        Intent intent = new Intent(this,ShowActivity.class);
        startActivity(intent);
        finish();
    }
    public void register(View view){
        if(view.getId() == R.id.btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
