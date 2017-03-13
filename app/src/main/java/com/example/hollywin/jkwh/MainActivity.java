package com.example.hollywin.jkwh;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    MapView mMapView = null;
    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        mLocationClient.setLocOption(InitLocOption());

        mLocationClient.start();// 开始定位

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);


    }
    private LocationClientOption InitLocOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//默认高精度定位模式
        option.setCoorType("bd09ll");//默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);//默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//默认false,设置是否使用gps
        option.setLocationNotify(true);//默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationPoiList(true);//默认false，设置是否需要POI结果
        option.setIgnoreKillProcess(false);//默认true不杀死进程，设置是否在stop的时候杀死进程
        option.SetIgnoreCacheException(false);//默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//默认false，设置是否需要过滤GPS仿真结果，默认需要
        return option;
    }

    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null){
                Toast.makeText(MainActivity.this,"location is null", Toast.LENGTH_SHORT).show();
                return;
            }
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());//创建一个图层选项
            OverlayOptions options = new MarkerOptions().position(latlng).icon(bitmapDescriptor);
            mBaiduMap.addOverlay(options);
            MapStatus mMapStatus = new MapStatus.Builder().target(latlng).zoom(12).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
        }
        @Override
        public void onConnectHotSpotMessage(String connectWifiMac, int hotSpotState){

        }

    }


//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        // BDLocation类，封装了定位SDK的定位结果，通过该类用户可以获取error code，位置的坐标，精度半径等信息
//        public void onReceiveLocation(BDLocation location) {
//            Log.e("!!!!!", "  ");//没有输出
//            if (location == null) {
//                return;
//            }
//        }
//
//
//
//
//    }

    @Override
    protected void onDestroy() {
        if(mLocationClient != null && mLocationClient.isStarted()){
            if(myListener != null){
                mLocationClient.unRegisterLocationListener(myListener);
            }
            mLocationClient.stop();
            mLocationClient = null;
        }
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}