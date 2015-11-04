package com.baidumap.innerac.baidumaptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidumap.innerac.baidumaptest.util.DBLink;

public class InfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MapView mMapView = null;
    BaiduMap mBaiduMap = null;

    SharedPreferences sp = null;
    SharedPreferences.Editor spEditor = null;

    boolean openTriaffic = false;
    boolean openHeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.point_search, (ViewGroup) findViewById(R.id.point_search));
                new AlertDialog.Builder(InfoActivity.this).setTitle("选择坐标").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv_x = (TextView) layout.findViewById(R.id.point_x);
                                TextView tv_y = (TextView) layout.findViewById(R.id.point_y);

                                jump(Double.parseDouble(tv_x.getText().toString()), Double.parseDouble(tv_y.getText().toString()));
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }


    private void init(){

        sp = getSharedPreferences("users", Activity.MODE_PRIVATE);
        getView();
//        getInfo();

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();



        jump(31.9207290000, 118.7942440000);

    }

    public void getInfo(View view){

        String myEmail = sp.getString("now#uid","");

        String myPhone = sp.getString(DBLink.link(myEmail,"phone"),"");


        TextView tv_nav_email = (TextView) findViewById(R.id.nav_my_email);
        TextView tv_nav_phone = (TextView) findViewById(R.id.nav_my_phone);


        tv_nav_email.setText(myEmail);
        tv_nav_phone.setText(myPhone);

    }
    private void getView(){
    }

    private void jump(double x,double y){
        LatLng hhu = new LatLng(x,y);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions()
                .position(hhu)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(hhu);
        mBaiduMap.animateMapStatus(msu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_map_normal) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.nav_map_satellite) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.nav_map_traffic) {
            openTriaffic = !openTriaffic;
            mBaiduMap.setTrafficEnabled(openTriaffic);
        } else if (id == R.id.nav_map_heat) {
            openHeat = !openHeat;
            mBaiduMap.setBaiduHeatMapEnabled(openHeat);
        } else if(id == R.id.nav_logout){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
