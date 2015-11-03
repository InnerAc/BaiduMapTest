package com.baidumap.innerac.baidumaptest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {


    Button btn_birth = null;
    TextView tv_birthday = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



    }

    private void init(){
        tv_birthday = (TextView) findViewById(R.id.birthday);
        btn_birth = (Button) findViewById(R.id.btn_birth);
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
                        int month = birth.getMonth()+1;
                        int date = birth.getDayOfMonth();

                        TextView tv_birthday = (TextView) findViewById(R.id.birthday);
                        tv_birthday.setText(year+"."+month+"."+date);
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
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
