package app.mat.tt.parcelableandyql;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    Result result;
    double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        result = getIntent().getParcelableExtra("result");
        lat = getIntent().getDoubleExtra("latitude",0);
        lng = getIntent().getDoubleExtra("longitude",0);
        Log.e("Tag", lat + "--"+ lng);
        WebView   webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(result.bussinessUrl);
        if (checkGoogleApiAvaibility()){
            initMap();
        }
    }
    void initMap(){
        SupportMapFragment supportMapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);
    }
    boolean checkGoogleApiAvaibility(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(DetailsActivity.this);

        if (result == ConnectionResult.SUCCESS){
            return  true;
        }else if (apiAvailability.isUserResolvableError(result)){
            Dialog dialog = apiAvailability.getErrorDialog(DetailsActivity.this,result,0);
            dialog.show();

        }else {
            Toast.makeText(this,"Can not connect to play services",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.addMarker(new MarkerOptions()
                .title(result.title)
                .position(new LatLng(result.latitude,result.longitude))
                .snippet(result.phone)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        mGoogleMap.addMarker(new MarkerOptions()
                .title(result.title)
                .position(new LatLng(lat,lng))
                .snippet(result.phone)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
       // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));
        final LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(result.latitude,result.longitude))
                .include(new LatLng(lat,lng))
                .build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds,1);
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
                mGoogleMap.setOnCameraChangeListener(null);
            }
        });
    }
}
