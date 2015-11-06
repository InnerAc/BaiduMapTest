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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidumap.innerac.baidumaptest.util.DBLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MapView mMapView = null;
    BaiduMap mBaiduMap = null;

    SharedPreferences sp = null;
    SharedPreferences.Editor spEditor = null;

    boolean openTriaffic = false;
    boolean openHeat = false;

    Map<String,String> citys = new HashMap<>();

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
                new AlertDialog.Builder(InfoActivity.this).setTitle("距离计算or经纬度定位").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv_x = (TextView) layout.findViewById(R.id.point_x);
                                TextView tv_y = (TextView) layout.findViewById(R.id.point_y);

                                TextView tv_x2 = (TextView) layout.findViewById(R.id.point_x2);
                                TextView tv_y2 = (TextView) layout.findViewById(R.id.point_y2);

                                String x = tv_x.getText().toString().trim();
                                String y = tv_y.getText().toString().trim();
                                String x2 = tv_x2.getText().toString().trim();
                                String y2 = tv_y2.getText().toString().trim();

                                if(!x.equals("") && !y.equals("")) {
                                    if(x2.equals("") || y2.equals("")) {
                                        jump(Double.parseDouble(x), Double.parseDouble(y));
                                    } else {
                                        distence(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(x2), Double.parseDouble(y2));
                                    }
                                }
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

        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);


        //左眼300，右眼450
        jump(450/10, 300/10);
        String tip ="本人左眼300,右眼450,定位到了黑海海边，西边是旧基利亚！！！！";
        showToast(tip);

    }

    private void getView(){
    }

    private void overLay(LatLng lat){
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions()
                .position(lat)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
    }
    private void jump(double x,double y){
        if(x >= 90 || x <=-90 || y >= 180 || y <= -180){
            showToast("经纬度不合法");
            return;
        }
        LatLng hhu = new LatLng(x,y);
        showToast("将要定位位置: "+y+" "+x);
        overLay(hhu);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(hhu);
        mBaiduMap.animateMapStatus(msu);
    }

    private void distence(double x,double y,double x1,double y1){
        if(x >= 90 || x <=-90 || y >= 180 || y <= -180){
            showToast("经纬度不合法");
            return;
        }
        if(x1 >= 90 || x1 <=-90 || y1 >= 180 || y1 <= -180){
            showToast("经纬度不合法");
            return;
        }

        LatLng p1 = new LatLng(x,y);
        LatLng p2 = new LatLng(x1,y1);
        overLay(p1);
        overLay(p2);
        double dis = DistanceUtil. getDistance(p1, p2)/1000;
        java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        OverlayOptions ooPolyline = new PolylineOptions().width(10).points(points);
        mBaiduMap.addOverlay(ooPolyline);

        OverlayOptions textOption = new TextOptions()
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text(df.format(dis)+"公里")
                .rotate(-30)
                .position(new LatLng((x+x1)/2,(y+y1)/2));
        mBaiduMap.addOverlay(textOption);

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(p2);
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
        } else if(id == R.id.nav_clear){
            mBaiduMap.clear();
        } else if(id == R.id.nav_other){
          showSearchCity();
        } else if(id == R.id.nav_logout){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showInfo(View view){
        View nav_header_info = findViewById(R.id.nav_header_info);
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setVisibility(View.GONE);

        String myEmail = sp.getString("now#uid","");
        String myPhone = sp.getString(DBLink.link(myEmail,"phone"),"");
        String mySex = sp.getString(DBLink.link(myEmail,"sex"),"");
        String myMotto = sp.getString(DBLink.link(myEmail,"motto"),"");
        String myNative = sp.getString(DBLink.link(myEmail,"native"),"");
        String myInterest = sp.getString(DBLink.link(myEmail,"interest"),"");
        String myBirth = sp.getString(DBLink.link(myEmail,"birthday"),"");

        TextView tv_nav_email = (TextView) findViewById(R.id.nav_my_email);
        TextView tv_nav_phone = (TextView) findViewById(R.id.nav_my_phone);
        tv_nav_email.setText(myEmail);
        tv_nav_phone.setText(myPhone);
        ((TextView) findViewById(R.id.nav_my_sex)).setText(mySex);
        ((TextView) findViewById(R.id.nav_my_motto)).setText(myMotto);
        ((TextView) findViewById(R.id.nav_my_native)).setText(myNative);
        ((TextView) findViewById(R.id.nav_my_interest)).setText(myInterest);
        ((TextView) findViewById(R.id.nav_my_birth)).setText(myBirth);
    }

    public void showSearchCity(){
        LayoutInflater inflater = getLayoutInflater();
        final View layout1 = inflater.inflate(R.layout.city_search, (ViewGroup) findViewById(R.id.layout_search_city));
        new AlertDialog.Builder(InfoActivity.this).setTitle("城市选择").setView(layout1)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et_searchCity = (EditText) layout1.findViewById(R.id.et_search_city);

                        String city = et_searchCity.getText().toString().trim();

                        SharedPreferences csp = getSharedPreferences("citys", Activity.MODE_PRIVATE);

                        String data = csp.getString(city, "").trim();

                        String msg = city+" 未查询到该城市!!";

                        if (!data.equals("")) {
                            String[] xy = data.split(",");

                            double x = Double.parseDouble(xy[1]);
                            double y = Double.parseDouble(xy[0]);

                            jump(x, y);

                            msg = city+" "+x+","+y;

                        }

                        showToast(msg);
                    }
                })
                .setNegativeButton("取消", null).show();
    }


    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
