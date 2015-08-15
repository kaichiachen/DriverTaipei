package hackntu2015.edu.yzu.drivertaipei;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.controller.DataListener;
import hackntu2015.edu.yzu.drivertaipei.controller.DataManager;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap;
    private RelativeLayout detailBar;
    private ImageView categoryIcon;
    private TextView categoryTitle;
    private ImageView categoryMood;
    private TextView categoryStatus;
    private Button categoryNavigation;
    private Context ctx;
    private LocationManager locationManager;
    private HashMap<Marker, NodeGas> gasData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        detailBar = (RelativeLayout)findViewById(R.id.detailBar);
        categoryTitle = (TextView)findViewById(R.id.title);
        categoryMood = (ImageView)findViewById(R.id.emotion);
        categoryStatus = (TextView)findViewById(R.id.status);

        mMap.setMyLocationEnabled(true);
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                //implementation
                setUpMap(location);
            }

            @Override
            public void onStatusChanged(String provider,int status,Bundle extras){}

            @Override
            public void onProviderEnabled(String provider){}
            @Override
            public void onProviderDisabled(String provider){}
        };

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        updateData();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(gasData.get(marker)!=null) {
                    NodeGas gas = gasData.get(marker);
                    categoryTitle.setText(gas.name);
                    if (gas.hasOil) {
                        categoryMood.setImageResource(R.mipmap.emoticon_happy);
                        if(gas.hasSelf) {
                            categoryStatus.setText("自助式");
                        } else{
                            categoryStatus.setText("非自助式");
                        }
                    } else {
                        categoryStatus.setText("非營業中");
                        categoryMood.setImageResource(R.mipmap.emoticon_sad);
                    }
                    showDetailBar();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    private void updateData(){
        DataManager.getInstance().setListener(new DataListener() {
            @Override
            public void onDataUpdate(List<NodeCarFlow> carFlowList, List<NodeParkingLot> parkingLotList, List<NodeTraffic> trafficList, List<NodeConstruct> constructsList, List<NodeGas> nodeGas) {
                setGasInfo(nodeGas);
            }

            @Override
            public void onDataConnectFailed(ErrorCode err) {

            }
        });
        DataManager.getInstance().updateData();

    }

    private void setGasInfo(List<NodeGas> nodeGase){
        gasData = new HashMap<Marker,NodeGas>();
        for(int i = 0;i< nodeGase.size();i++) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(nodeGase.get(i).lat, nodeGase.get(i).lon))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_petrolstation)));
            gasData.put(marker,nodeGase.get(i));
        }
    }

    private void showDetailBar(){
        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                detailBar.setAlpha(1);
            }
        });
        detailBar.startAnimation(amAlpha);
    }

    private void setUpMap(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.gas_petrol_station_md)));

    }


}
