package hackntu2015.edu.yzu.drivertaipei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.navdrawer.SimpleSideDrawer;

import java.util.HashMap;
import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.controller.DataController;
import hackntu2015.edu.yzu.drivertaipei.controller.DataListener;
import hackntu2015.edu.yzu.drivertaipei.controller.DataManager;
import hackntu2015.edu.yzu.drivertaipei.utils.AutoResizeTextView;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;
import hackntu2015.edu.yzu.drivertaipei.utils.Utils;

public class MainActivity extends ActionBarActivity {

    private Context ctx;
    private LocationManager locationManager;
    private GoogleMap mMap;

    private RelativeLayout detailBar;
    private LinearLayout detailLinearLayout;
    private Button categoryNavigation;
    private Button categoryPayMent;

    private LinearLayout filterLayout;
    private CheckBox gasCheckBox;
    private CheckBox parkingLotCheckBox;
    private CheckBox constructCheckBox;
    private CheckBox carFlowCheckBox;

    private HashMap<Marker, NodeGas> gasData;
    private HashMap<Marker, NodeParkingLot> parkingLotData;
    private HashMap<Marker, NodeConstruct> constructData;
    private HashMap<GroundOverlay, NodeCarFlow> carFlowData;
    private HashMap<Marker, NodeTraffic> trafficData;

    private Handler uiHandler = new Handler();
    private Marker mSelectMarker;

    private boolean insideScale = true;
    private boolean gasIsCheck = true;
    private boolean parkingLotIsCheck = true;
    private boolean constructIsCheck = true;
    private boolean carFlowIsCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ctx = this;

        setActionBar();

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        detailBar = (RelativeLayout)findViewById(R.id.detailBar);
        detailLinearLayout = (LinearLayout)findViewById(R.id.detailLinearLayout);
        categoryNavigation = (Button)findViewById(R.id.navigationButton);
        categoryPayMent = (Button)findViewById(R.id.parkingMoneyButton);

        filterLayout = (LinearLayout)findViewById(R.id.filter_layout);
        gasCheckBox = (CheckBox)findViewById(R.id.gas_checkBox);
        parkingLotCheckBox = (CheckBox)findViewById(R.id.parkingLot_checkBox);
        carFlowCheckBox = (CheckBox)findViewById(R.id.carFlow_checkBox);
        constructCheckBox = (CheckBox)findViewById(R.id.construct_checkBox);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });
        mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        updateData();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mSelectMarker = marker;
                if (gasData.get(marker) != null) {
                    showCard(gasData.get(marker));
                } else if (parkingLotData.get(marker) != null) {
                    showCard(parkingLotData.get(marker));
                } else if (constructData.get(marker) != null) {
                    showCard(constructData.get(marker));
                } else if (trafficData.get(marker) != null){
                    showCard(trafficData.get(marker));
                }
                return true;
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (detailBar.getVisibility() == View.VISIBLE)
                    removeCard();
                if (cameraPosition.zoom < 16.5) {
                    insideScale = true;
                } else {
                    insideScale = true;
                }
               // updateMarker();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (detailBar.getVisibility() == View.VISIBLE)
                    removeCard();
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.INVISIBLE);
                } else {
                    filterLayout.setVisibility(View.VISIBLE);
                }
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
        filterImplement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider,int status,Bundle extras){}

            @Override
            public void onProviderEnabled(String provider){}
            @Override
            public void onProviderDisabled(String provider){}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar. LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(mCustomView,layoutParams);
        actionBar.setDisplayShowCustomEnabled(true);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageButton filter = (ImageButton)mCustomView.findViewById(R.id.btn_filter);
        ImageButton menu = (ImageButton)mCustomView.findViewById(R.id.btn_menu);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.INVISIBLE);
                } else {
                    filterLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        final SimpleSideDrawer mMenu = new SimpleSideDrawer(this);
        mMenu.setLeftBehindContentView(R.layout.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.toggleLeftDrawer();
            }
        });
        ImageView menu_top = (ImageView)mMenu.findViewById(R.id.menu_top);
        if(Utils.isNight()) {
            menu_top.setBackgroundResource(R.mipmap.menu_topimage_night);
        } else{
            menu_top.setBackgroundResource(R.mipmap.menu_topimage_day);
        }
        ListView listView = (ListView)mMenu.findViewById(R.id.listView);
        listView.setAdapter(new MenuAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void updateMarker(){
        if(insideScale){
            if(gasIsCheck) {
                for (Marker marker : gasData.keySet()) {
                    marker.setVisible(true);
                }
            } else {
                for (Marker marker : gasData.keySet()) {
                    marker.setVisible(false);
                }
            }
            if(parkingLotIsCheck) {
                for (Marker marker : parkingLotData.keySet()) {
                    marker.setVisible(true);
                }
            } else {
                for (Marker marker : parkingLotData.keySet()) {
                    marker.setVisible(false);
                }
            }
            if(constructIsCheck) {
                for (Marker marker : constructData.keySet()) {
                    marker.setVisible(true);
                }
                for (Marker marker : trafficData.keySet()) {
                    marker.setVisible(true);
                }
            } else {
                for (Marker marker : constructData.keySet()) {
                    marker.setVisible(false);
                }
                for (Marker marker : trafficData.keySet()) {
                    marker.setVisible(false);
                }
            }
            if(carFlowIsCheck) {
                for (GroundOverlay canvas : carFlowData.keySet()) {
                    canvas.setVisible(true);
                }
            } else {
                for (GroundOverlay canvas : carFlowData.keySet()) {
                    canvas.setVisible(false);
                }
            }

        } else {
            invisibaleizeMarker();
        }
    }

    private void invisibaleizeMarker(){
        for (Marker marker : gasData.keySet()) {
            marker.setVisible(false);
        }
        for (Marker marker : parkingLotData.keySet()) {
            marker.setVisible(false);
        }
        for (Marker marker : constructData.keySet()) {
            marker.setVisible(false);
        }
//        for (GroundOverlay canvas : carFlowData.keySet()) {
//            canvas.setVisible(false);
//        }
    }

    private void filterImplement(){
        gasCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && insideScale) {
                    for (Marker marker : gasData.keySet()) {
                        marker.setVisible(true);
                    }
                } else {
                    for (Marker marker : gasData.keySet()) {
                        marker.setVisible(false);
                    }
                }
                gasIsCheck = isChecked;
            }
        });

        parkingLotCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && insideScale) {
                    for (Marker marker : parkingLotData.keySet()) {
                        marker.setVisible(true);
                    }
                } else {
                    for (Marker marker : parkingLotData.keySet()) {
                        marker.setVisible(false);
                    }
                }
                parkingLotIsCheck = isChecked;
            }
        });

        constructCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && insideScale) {
                    for (Marker marker : constructData.keySet()) {
                        marker.setVisible(true);
                    }
                    for (Marker marker : trafficData.keySet()) {
                        marker.setVisible(true);
                    }
                } else {
                    for (Marker marker : constructData.keySet()) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : trafficData.keySet()) {
                        marker.setVisible(false);
                    }
                }
                constructIsCheck = isChecked;
            }
        });

        carFlowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && insideScale) {
                    for (GroundOverlay canvas : carFlowData.keySet()) {
                        canvas.setVisible(true);
                    }
                } else {
                    for (GroundOverlay canvas : carFlowData.keySet()) {
                        canvas.setVisible(false);
                    }
                }
                carFlowIsCheck = isChecked;
            }
        });
    }

    private void updateData(){
        DataManager.getInstance().setListener(new DataListener() {

            @Override
            public void onParkingLotDataDownloadComplete() {

            }

            @Override
            public void onGasDataDownloadComplete() {

            }

            @Override
            public void onConstructDataDownloadComplete() {

            }

            @Override
            public void onCarFlowDataDownloadComplete() {

            }

            @Override
            public void onTrafficDataDownloadComplete() {

            }

            @Override
            public void onDataDownloadFailed(ErrorCode err, DataController.DataType dataType) {

            }

            @Override
            public void onGasDataUpdate(List<NodeGas> gasList) {
                setGasInfo(gasList);
            }

            @Override
            public void onParkingLotDataUpdate(List<NodeParkingLot> parkingLotList) {
                setParkingInfo(parkingLotList);
            }

            @Override
            public void onConstructDataUpdate(List<NodeConstruct> constructList) {
                setConstructInfo(constructList);
            }

            @Override
            public void onCarFlowDataUpdate(List<NodeCarFlow> carFlowList) {
                //setCarFlowInfo(carFlowList);
            }

            @Override
            public void onTrafficDataUpdate(List<NodeTraffic> trafficList) {
                setTrafficInfo(trafficList);
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
        AutoResizeTextView categoryTitle;
        ImageView categoryMood;
        AutoResizeTextView categoryStatus;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryIcon.setLayoutParams(layout);
        categoryIcon.setImageResource(R.mipmap.gas_petrol_station_md);
        detailLinearLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
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

        categoryStatus = new AutoResizeTextView(ctx);
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
        AutoResizeTextView categoryCarCount;
        ImageView categoryMotorIcon;
        AutoResizeTextView categoryMotrorCount;
        ImageView categoryMood;
        AutoResizeTextView categoryStatus;

        categoryCarIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryCarIcon.setImageResource(R.mipmap.parkingdark);
        categoryCarIcon.setLayoutParams(layout);
        detailLinearLayout.addView(categoryCarIcon);

        categoryCarCount = new AutoResizeTextView(ctx);
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

        categoryMotrorCount = new AutoResizeTextView(ctx);
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

        categoryStatus = new AutoResizeTextView(ctx);
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

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
                payDialog.setView(dialoglayout);
                final AlertDialog dialog = payDialog.show();
                dialog.getWindow().setLayout(1000,1200);
                TextView content = (TextView)dialoglayout.findViewById(R.id.dialog_content);
                content.setText(nodeParkingLot.payDes);
                Button closebtn = (Button)dialoglayout.findViewById(R.id.dialog_close);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
        AutoResizeTextView categoryTitle;
        ImageView categoryMood;
        AutoResizeTextView categoryStatus;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryIcon.setLayoutParams(layout);
        if(nodeConstruct.isToday){ categoryIcon.setImageResource(R.mipmap.warning); }
        else{ categoryIcon.setImageResource(R.mipmap.constructionsite); }

        detailLinearLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
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

        categoryStatus = new AutoResizeTextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.CENTER);
        detailLinearLayout.addView(categoryStatus);

        categoryStatus.setText(nodeConstruct.completeDate + "完成");
        categoryMood.setImageResource(R.mipmap.emoticon_happy);
        categoryStatus.setTextColor(Color.parseColor("#e8a032"));

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

    private void setCarFlowInfo(List<NodeCarFlow> nodeCarFlows){
        carFlowData = new HashMap<GroundOverlay,NodeCarFlow>();
        for(int i = 0;i< nodeCarFlows.size();i++) {
            GroundOverlay overlay = null;
            if(nodeCarFlows.get(i).level == NodeCarFlow.carFlowLevel.LOW){
                overlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.mipmap.road_red))
                        .position(nodeCarFlows.get(i).centerLocation, nodeCarFlows.get(i).width, nodeCarFlows.get(i).height));
            } else if(nodeCarFlows.get(i).level == NodeCarFlow.carFlowLevel.MEDIUM){
                overlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.mipmap.road_red))
                        .position(nodeCarFlows.get(i).centerLocation, nodeCarFlows.get(i).width, nodeCarFlows.get(i).height));
            } else {
                overlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.mipmap.road_green))
                        .position(nodeCarFlows.get(i).centerLocation, nodeCarFlows.get(i).width, nodeCarFlows.get(i).height));
            }
            carFlowData.put(overlay,nodeCarFlows.get(i));
        }
    }

    private void setTrafficInfo(List<NodeTraffic> nodeTraffic){
        trafficData = new HashMap<Marker,NodeTraffic>();
        for (int i =0 ;i< nodeTraffic.size();i++){
            Marker marker = null;
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(nodeTraffic.get(i).lat, nodeTraffic.get(i).lon))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_warning)));
            trafficData.put(marker, nodeTraffic.get(i));
        }
    }


    private void showCard(final NodeTraffic nodeTraffic){
        detailLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        AutoResizeTextView categoryTitle;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryIcon.setLayoutParams(layout);
        categoryIcon.setImageResource(R.mipmap.constructionsite);

        detailLinearLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.08f));
        categoryTitle.setText(nodeTraffic.status);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER | Gravity.LEFT);
        detailLinearLayout.addView(categoryTitle);

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
