package hackntu2015.edu.yzu.drivertaipei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

    private Context ctx;
    private LocationManager locationManager;
    private GoogleMap mMap;

    private RelativeLayout detailBar;
    private LinearLayout detailLinearLayout;
    private Button categoryNavigation;
    private Button categoryPayMent;

    private HashMap<Marker, NodeGas> gasData;
    private HashMap<Marker, NodeParkingLot> parkingLotData;
    private HashMap<Marker, NodeConstruct> constructData;

    private Handler uiHandler = new Handler();
    private Marker mSelectMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        detailBar = (RelativeLayout)findViewById(R.id.detailBar);
        detailLinearLayout = (LinearLayout)findViewById(R.id.detailLinearLayout);
        categoryNavigation = (Button)findViewById(R.id.navigationButton);
        categoryPayMent = (Button)findViewById(R.id.parkingMoneyButton);

        mMap.setMyLocationEnabled(true);
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
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
                mSelectMarker = marker;
                if (gasData.get(marker) != null) {
                    showCard(gasData.get(marker));
                } else if (parkingLotData.get(marker) != null) {
                    showCard(parkingLotData.get(marker));
                } else if (constructData.get(marker) != null){
                    showCard(constructData.get(marker));
                }
                return true;
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
               if(detailBar.getVisibility() == View.VISIBLE)
                    removeCard();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(detailBar.getVisibility() == View.VISIBLE)
                    removeCard();
            }
        });

        categoryNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saddr = "saddr=" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude();
                String daddr = "daddr=" + mSelectMarker.getPosition().latitude + "," + mSelectMarker.getPosition().longitude;
                String uriString = "http://maps.google.com/maps?" + saddr + "&" + daddr;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
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
            public void onDataUpdate(List<NodeCarFlow> carFlowList, List<NodeParkingLot> parkingLotList, List<NodeTraffic> trafficList, List<NodeConstruct> constructsList, List<NodeGas> gasList) {
                setGasInfo(gasList);
                setParkingInfo(parkingLotList);
                setConstructInfo(constructsList);
            }

            @Override
            public void onDataConnectFailed(ErrorCode err) {

            }
        });
        DataManager.getInstance().updateData();

    }

    private void removeCard(){
        Animation amAlpha = new AlphaAnimation(1.0f, 0.0f);
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
                detailBar.setVisibility(View.INVISIBLE);
                categoryNavigation.setVisibility(View.INVISIBLE);
                categoryPayMent.setVisibility(View.INVISIBLE);
            }
        });
        detailBar.startAnimation(amAlpha);
        if(categoryNavigation.getVisibility() == View.VISIBLE)
            categoryNavigation.startAnimation(amAlpha);
        if(categoryPayMent.getVisibility() == View.VISIBLE)
            categoryPayMent.startAnimation(amAlpha);
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

    private void showCard(NodeGas nodeGas){
        detailLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        TextView categoryTitle;
        ImageView categoryMood;
        TextView categoryStatus;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryIcon.setLayoutParams(layout);
        categoryIcon.setImageResource(R.mipmap.gas_petrol_station_md);
        detailLinearLayout.addView(categoryIcon);

        categoryTitle = new TextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryTitle.setText(nodeGas.name);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER);
        detailLinearLayout.addView(categoryTitle);

        categoryMood = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMood.setLayoutParams(layout);
        detailLinearLayout.addView(categoryMood);

        categoryStatus = new TextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextColor(Color.RED);
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.LEFT | Gravity.CENTER);
        detailLinearLayout.addView(categoryStatus);

        if (nodeGas.hasOil) {
            categoryMood.setImageResource(R.mipmap.emoticon_happy);
            if (nodeGas.hasSelf) {
                categoryStatus.setText("自助式");
            } else {
                categoryStatus.setText("非自助式");
            }
            categoryStatus.setTextColor(Color.parseColor("#e8a032"));
        } else {
            categoryStatus.setText("非營業中");
            categoryMood.setImageResource(R.mipmap.emoticon_sad);
            categoryStatus.setTextColor(Color.RED);
        }

        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                detailBar.setVisibility(View.VISIBLE);
                categoryNavigation.setVisibility(View.VISIBLE);
                categoryPayMent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        detailBar.startAnimation(amAlpha);
        categoryNavigation.startAnimation(amAlpha);
    }

    private void setParkingInfo(List<NodeParkingLot> nodeParkingLots){
        parkingLotData = new HashMap<Marker,NodeParkingLot>();
        for(int i = 0;i< nodeParkingLots.size();i++) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(nodeParkingLots.get(i).lat, nodeParkingLots.get(i).lon))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_parkinglot)));
            parkingLotData.put(marker,nodeParkingLots.get(i));
        }
    }

    private void showCard(final NodeParkingLot nodeParkingLot){
        detailLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryCarIcon;
        TextView categoryCarCount;
        ImageView categoryMotorIcon;
        TextView categoryMotrorCount;
        ImageView categoryMood;
        TextView categoryStatus;

        categoryCarIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryCarIcon.setImageResource(R.mipmap.parkingdark);
        categoryCarIcon.setLayoutParams(layout);
        detailLinearLayout.addView(categoryCarIcon);

        categoryCarCount = new TextView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.14f);
        if(nodeParkingLot.availableCar < 10)
            layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.15f);
        categoryCarCount.setLayoutParams(layout);
        categoryCarCount.setText("" + nodeParkingLot.availableCar);
        categoryCarCount.setTextColor(Color.BLACK);
        categoryCarCount.setGravity(Gravity.CENTER);
        detailLinearLayout.addView(categoryCarCount);

        categoryMotorIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMotorIcon.setLayoutParams(layout);
        categoryMotorIcon.setImageResource(R.mipmap.parking_bike_dark);
        detailLinearLayout.addView(categoryMotorIcon);

        categoryMotrorCount = new TextView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.14f);
        if(nodeParkingLot.availableMotor < 10)
            layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.15f);
        categoryMotrorCount.setLayoutParams(layout);
        categoryMotrorCount.setText("" + nodeParkingLot.availableMotor);
        categoryMotrorCount.setTextColor(Color.BLACK);
        categoryMotrorCount.setGravity(Gravity.CENTER);
        detailLinearLayout.addView(categoryMotrorCount);

        categoryMood = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMood.setLayoutParams(layout);
        detailLinearLayout.addView(categoryMood);

        categoryStatus = new TextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextColor(Color.RED);
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.LEFT | Gravity.CENTER);
        detailLinearLayout.addView(categoryStatus);

        if (nodeParkingLot.availableCar > 0 ) {
            categoryMood.setImageResource(R.mipmap.emoticon_happy_green);
            categoryStatus.setText("有車位");
            categoryStatus.setTextColor(Color.parseColor("#22ac38"));
        } else if(nodeParkingLot.availableMotor > 0){
            categoryMood.setImageResource(R.mipmap.emoticon_happy_green);
            categoryStatus.setText("有車位");
            categoryStatus.setTextColor(Color.parseColor("#22ac38"));
        } else{
            categoryMood.setImageResource(R.mipmap.emoticon_sad);
            categoryStatus.setText("無車位");
            categoryStatus.setTextColor(Color.RED);
        }


        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                detailBar.setVisibility(View.VISIBLE);
                categoryNavigation.setVisibility(View.VISIBLE);
                categoryPayMent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        detailBar.startAnimation(amAlpha);
        categoryNavigation.startAnimation(amAlpha);
        categoryPayMent.startAnimation(amAlpha);
        categoryPayMent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder payDialog = new AlertDialog.Builder(ctx);
                payDialog.setMessage(nodeParkingLot.payDes)
                        .setNegativeButton("關閉", null);
                final AlertDialog dialog = payDialog.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                    }
                });
                payDialog.show();
            }
        });
    }

    private void setConstructInfo(List<NodeConstruct> nodeConstruct){
        constructData = new HashMap<Marker,NodeConstruct>();
        for(int i = 0;i< nodeConstruct.size();i++) {
            Marker marker = null;
            if(nodeConstruct.get(i).isToday) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(nodeConstruct.get(i).lat, nodeConstruct.get(i).lon))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_warning)));
            } else {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(nodeConstruct.get(i).lat, nodeConstruct.get(i).lon))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_constructionsite)));
            }
            constructData.put(marker,nodeConstruct.get(i));
        }
    }

    private void showCard(final NodeConstruct nodeConstruct){
        detailLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        TextView categoryTitle;
        ImageView categoryMood;
        TextView categoryStatus;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryIcon.setLayoutParams(layout);
        if(nodeConstruct.isToday){ categoryIcon.setImageResource(R.mipmap.warning); }
        else{ categoryIcon.setImageResource(R.mipmap.constructionsite); }

        detailLinearLayout.addView(categoryIcon);

        categoryTitle = new TextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.08f));
        categoryTitle.setText(nodeConstruct.status);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER | Gravity.LEFT);
        detailLinearLayout.addView(categoryTitle);

        categoryMood = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryMood.setLayoutParams(layout);
        detailLinearLayout.addView(categoryMood);

        categoryStatus = new TextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.CENTER);
        detailLinearLayout.addView(categoryStatus);

        if (nodeConstruct.isToday) {
            categoryMood.setImageResource(R.mipmap.emoticon_sad);
            categoryStatus.setText("今日發生");
            categoryStatus.setTextColor(Color.RED);
        } else {
            categoryStatus.setText(nodeConstruct.completeDate+"完成");
            categoryMood.setImageResource(R.mipmap.emoticon_happy);
            categoryStatus.setTextColor(Color.parseColor("#e8a032"));
        }

        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                detailBar.setVisibility(View.VISIBLE);
                categoryNavigation.setVisibility(View.INVISIBLE);
                categoryPayMent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
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
