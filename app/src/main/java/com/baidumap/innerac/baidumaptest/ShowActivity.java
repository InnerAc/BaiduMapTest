package com.baidumap.innerac.baidumaptest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class ShowActivity extends AppCompatActivity {


    MapView mMapView = null;
    BaiduMap mBaiduMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.point_search, (ViewGroup) findViewById(R.id.point_search));
                new AlertDialog.Builder(ShowActivity.this).setTitle("选择坐标").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv_x = (TextView) layout.findViewById(R.id.point_x);
                                TextView tv_y = (TextView) layout.findViewById(R.id.point_y);

                                jump(Double.parseDouble(tv_x.getText().toString()),Double.parseDouble(tv_y.getText().toString()));
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });
        init();
    }

    private void init(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        jump(31.9207290000,118.7942440000);

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
    protected void onDestroy(){
        super.onDestroy();

        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

}
