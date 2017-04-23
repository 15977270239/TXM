package com.txm.www;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class MainActivity extends Activity implements TencentLocationListener{
    private TencentLocationManager mLocationManager;
    private TencentLocationRequest locationRequest;
    private TextView city ,district;
    String lblocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = (TextView)findViewById(R.id.city);
        district = (TextView)findViewById(R.id.aear);
        for (int i=0;i<20;i++){
            int num = i;
        }
        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        TencentLocationRequest request = TencentLocationRequest.create()
                .setInterval(5000)
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        mLocationManager.requestLocationUpdates(request, this);
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String s) {
        //获取定位结果
        if (error == TencentLocation.ERROR_OK) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double direction = location.getExtra().getDouble(TencentLocation.EXTRA_DIRECTION);
            lblocation = location.getCity();
            city.setText(lblocation);
            district.setText(location.getDistrict());
            StringBuilder sb = new StringBuilder();
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=").append(location.getLongitude()).append(",精度=").append(location.getAccuracy()).append("), 来源=").append(location.getProvider()).append(", 地址=").append(location.getAddress());

            Toast.makeText(this,"当前位置"+sb.toString(),Toast.LENGTH_LONG).show();
            Log.v("lblocation=",sb.toString());
        } else {
            Toast.makeText(this,"定位失败",Toast.LENGTH_LONG).show();
        }
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(this);
    }
}
