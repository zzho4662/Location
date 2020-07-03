package com.test.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class MainActivity extends AppCompatActivity {

    private String clientId= "eb9yao3lut";
    private String clientSecret = "gknYHplief0Uzgh3PsTYgWdrLgsmrtnIUBbgYkgM";

    private String baseUrl = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=300&h=300&level=16";
    private LocationManager locationManager;
    private LocationListener locationListener;
    TextView txtGps;
    ImageView imgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGps = findViewById(R.id.txtGps);
        imgMap = findViewById(R.id.imgMap);

        //안드로이드의 위치를 관리하는 객체가 LcationManager다
        //따라서 안드로이드 시스템에 로케이션서비스를 이앱이 사용하겠다고
        //로케이션 서비스를 여청하여 로케이션 매니저 변수에 저장해 줘야한다.
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //폰의 위치가 바뀔때마다 해주고싶은 일을 여기에 작성
                //안드로이드가 폰의 위치 바뀌때마다 이 메소드를 호출해줍니다.
                Log.i("AAA", location.toString());
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                String url = baseUrl + "&center="+lng+","+lat;

                GlideUrl glideUrl = new GlideUrl(url,
                        new LazyHeaders.Builder().addHeader("X-NCP-APIGW-API-KEY-ID", clientId).
                                addHeader("X-NCP-APIGW-API-KEY", clientSecret).build());

                Glide.with(MainActivity.this).load(glideUrl).into(imgMap);

                txtGps.setText("Center : "+lng+","+lat);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //유저한테 이앱은 위치기반 권한이 있어야 한다고 알려야 한다.
            //유저가 권한 설정을 하고 나면 처리해야 할 코드를 작성하기 위해서
            //requestCode 값을 설정한다.
            ActivityCompat.requestPermissions(MainActivity.this,
                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                   Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000,
                0,
                locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000,
                    0,
                    locationListener);
        }
    }
}
