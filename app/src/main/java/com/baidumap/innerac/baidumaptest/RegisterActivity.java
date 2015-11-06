package com.baidumap.innerac.baidumaptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidumap.innerac.baidumaptest.util.DBLink;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    TextView tv_birthday = null;
    EditText et_email = null;
    EditText et_pwd1 = null;
    EditText et_pwd2 = null;
    EditText et_motto = null;
    EditText et_native = null;
    EditText et_phone = null;
    RadioButton rb_man = null;
    Spinner spr_interest = null;


    ArrayAdapter adapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }

    private void init(){
        getView();
        initSpinner();

    }

    private void getView(){
        tv_birthday = (TextView) findViewById(R.id.birthday);
        et_email = (EditText) findViewById(R.id.email);
        et_pwd1 = (EditText) findViewById(R.id.pwd1);
        et_pwd2 = (EditText) findViewById(R.id.pwd2);
        et_motto = (EditText) findViewById(R.id.motto);
        et_native = (EditText) findViewById(R.id.nativePlace);
        et_phone = (EditText) findViewById(R.id.phone);
        rb_man = (RadioButton) findViewById(R.id.man);
    }

    private void initSpinner(){
        spr_interest = (Spinner) findViewById(R.id.interest);
        adapter = ArrayAdapter.createFromResource(this,R.array.interest,R.layout.support_simple_spinner_dropdown_item);
        spr_interest.setAdapter(adapter);
    }

    public void dialog_birth(View view){
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.birthday, (ViewGroup) findViewById(R.id.dialog_birthday));
        new AlertDialog.Builder(this).setTitle("选择生日").setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker birth = (DatePicker) layout.findViewById(R.id.date_birthdays);
                        int year = birth.getYear();
                        int month = birth.getMonth() + 1;
                        int date = birth.getDayOfMonth();

                        Calendar curDate = Calendar.getInstance();

                        int nowYear = curDate.get(Calendar.YEAR);
                        int nowMonth = curDate.get(Calendar.MONTH)+1;
                        int nowDate = curDate.get(Calendar.DATE);

                        String dates = year + "-" + month + "-" + date;

                        if (year > nowYear || (year == nowYear && month > nowMonth) || (year == nowYear && month == nowMonth && date > nowDate)) {
                                    dates = "ERROR!!";
                        }

                        TextView tv_birthday = (TextView) findViewById(R.id.birthday);
                        tv_birthday.setText(dates);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    public void registers(View view){
        if(view.getId() == R.id.btn_insert) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{

            String msg = isLegal();

            if(msg == null){
                showToast("注册成功");

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                showToast(msg);
            }


        }
    }

    public String isLegal(){
        String ans = null;

        String s_birth = tv_birthday.getText().toString().trim();
        String s_email = et_email.getText().toString().trim();
        String s_pwd1 = et_pwd1.getText().toString().trim();
        String s_pwd2 = et_pwd2.getText().toString().trim();
        String s_motto = et_motto.getText().toString().trim();
        String s_native = et_native.getText().toString().trim();
        String s_phone = et_phone.getText().toString().trim();
        boolean b_man = rb_man.isChecked();
        String s_sex = "女";
        String s_interest = spr_interest.getSelectedItem().toString();

        SharedPreferences sp = getSharedPreferences("users", Activity.MODE_PRIVATE);
        String db_email = sp.getString(s_email,"null");

        if(s_email.equals("")){
            ans = "邮箱或用户名不得为空";
            return ans;
        }
        if(s_email.indexOf("@") != -1){
            Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$");
            Matcher matcher = pattern.matcher(s_email);
            if(!matcher.matches()){
                ans = "邮箱格式非法!!";
                return ans;
            }
        }
        if(!db_email.equals("null")){
            ans = "该邮箱或用户名已存在";
            return ans;
        }
        if(s_pwd1.equals("")){
            ans="密码不得为空";
            return ans;
        }
        if(s_motto.equals("")){
            ans = "座右铭不得为空";
            return ans;
        }
        if(s_native.equals("")){
            ans = "籍贯不得为空";
            return ans;
        }
        if(s_phone.equals("") || s_phone.length()!= 11){
            ans="手机号码不合法!!";
            return ans;
        }
        if(s_interest.equals("")){
            ans = "兴趣不得为空";
            return ans;
        }
        if(!s_pwd1.equals(s_pwd2)){
            ans = "两次密码输入不匹配";
            return ans;
        }
        if(b_man){
            s_sex = "男";
        }
        if(s_birth.equals("ERROR!!")){
            ans = "出生日期不合法";
            return ans;
        }


        SharedPreferences.Editor spEditor = sp.edit();

        spEditor.putString(s_email,"ok");
        spEditor.putString(DBLink.link(s_email,"pwd"),s_pwd1);
        spEditor.putString(DBLink.link(s_email,"motto"),s_motto);
        spEditor.putString(DBLink.link(s_email,"native"),s_native);
        spEditor.putString(DBLink.link(s_email,"phone"),s_phone);
        spEditor.putString(DBLink.link(s_email,"interest"),s_interest);
        spEditor.putString(DBLink.link(s_email,"sex"),s_sex);
        spEditor.putString(DBLink.link(s_email,"birthday"),s_birth);

        spEditor.commit();

        return ans;
    }

    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
