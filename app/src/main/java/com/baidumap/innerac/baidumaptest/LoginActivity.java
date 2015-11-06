package com.baidumap.innerac.baidumaptest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.baidumap.innerac.baidumaptest.util.DBLink;

public class LoginActivity extends AppCompatActivity {


    CheckBox cb_saveUid = null;
    CheckBox cb_savePwd = null;
    EditText et_uid = null;
    EditText et_pwd = null;

    SharedPreferences sp = null;
    SharedPreferences.Editor spEditor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        sp = getSharedPreferences("users", Activity.MODE_PRIVATE);
        spEditor = sp.edit();
        getView();
    }

    private void getView(){
        cb_saveUid = (CheckBox) findViewById(R.id.cbSaveUid);
        cb_savePwd = (CheckBox) findViewById(R.id.cbSavePwd);
        et_uid = (EditText) findViewById(R.id.uid);
        et_pwd = (EditText) findViewById(R.id.pwd);

        et_uid.setText(sp.getString("tmp#uid",""));
        et_pwd.setText(sp.getString("tmp#pwd",""));

    }



    public void login(View view){

        String s_uid = et_uid.getText().toString().trim();
        String s_pwd = et_pwd.getText().toString().trim();

        String db_uid = sp.getString(s_uid, "null");
        String db_pwd = sp.getString(DBLink.link(s_uid,"pwd"),"");

        if(db_uid.equals("null")){
            showToast("用户名或邮箱不存在!!");
            spEditor.putString("tmp#uid", "");
            spEditor.putString("tmp#pwd","");
            spEditor.commit();
            return;
        }
        if(!db_pwd.equals(s_pwd)){
            showToast("密码错误!!");
            spEditor.putString("tmp#uid", "");
            spEditor.putString("tmp#pwd","");
            spEditor.commit();
            return;
        }

        if(cb_saveUid.isChecked()){
            spEditor.putString("tmp#uid",s_uid);
        }else {
            spEditor.putString("tmp#uid","");
        }
        if(cb_savePwd.isChecked()){
            spEditor.putString("tmp#pwd",s_pwd);
        }else {
            spEditor.putString("tmp#pwd","");
        }
        spEditor.putString("now#uid",s_uid);

        spEditor.commit();

        enterMap();
    }

    private void enterMap(){
        Intent intent = new Intent(this,InfoActivity.class);
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

    public void checkSave(View view){
        if(view.getId() == R.id.cbSavePwd){
            if(cb_savePwd.isChecked())
                cb_saveUid.setChecked(true);
        }else if(view.getId() == R.id.cbSaveUid){
            if(!cb_saveUid.isChecked()){
                cb_savePwd.setChecked(false);
            }
        }
    }

    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
