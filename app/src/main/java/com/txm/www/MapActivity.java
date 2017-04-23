package com.txm.www;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;

public class MapActivity extends Activity implements LocationSource,TencentLocationListener {

    private TencentLocationRequest locationRequest;
    private TencentLocationManager locationManager;

    private MapView mapView;
    private TencentMap tencentMap;

    private OnLocationChangedListener mChangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) this.findViewById(R.id.map);
        init();
    }
    private void init(){
        tencentMap = mapView.getMap();
        tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
        CircleOptions circleOption = new CircleOptions();
        circleOption.strokeColor(Color.BLACK);
        circleOption.strokeWidth(1.0f);
        circleOption.fillColor(Color.argb(100, 0, 0, 180));
        circleOption.radius(5);
        tencentMap.addCircle(circleOption);

        locationRequest = TencentLocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
        locationRequest.setAllowCache(true);
        locationManager = TencentLocationManager.getInstance(this);

        tencentMap.setLocationSource(this);
        tencentMap.setMyLocationEnabled(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(locationRequest, this);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView = null;
        locationManager = null;
    }

    @Override
    public void activate(OnLocationChangedListener arg0) {
        mChangedListener = arg0;
        int err = locationManager.requestLocationUpdates(locationRequest, this);
        switch (err){
            case 1:
                setTitle("设备缺少使用腾讯定位服务需要的基本条件");
                break;
            case 2:
                setTitle("manifest 中配置的 key 不正确");
                break;
            case 3:
                setTitle("自动加载libtencentloc.so失败");
                break;
        }
    }

    @Override
    public void deactivate() {
        //结束定位
        locationManager.removeUpdates(this);
        mChangedListener = null;
        locationRequest = null;
        locationManager = null;
    }

    @Override
    public void onLocationChanged(TencentLocation arg0, int arg1, String arg2) {

        if (arg1 == TencentLocation.ERROR_OK && mChangedListener != null) {
            Location location = new Location(arg0.getProvider());
            location.setLatitude(arg0.getLatitude());
            location.setLongitude(arg0.getLongitude());
            location.setAccuracy(arg0.getAccuracy());
            mChangedListener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusUpdate(String arg0, int arg1, String arg2) {

    }
}
