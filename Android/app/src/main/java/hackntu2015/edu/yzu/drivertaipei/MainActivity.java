package hackntu2015.edu.yzu.drivertaipei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.navdrawer.SimpleSideDrawer;

import java.util.ArrayList;
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
import hackntu2015.edu.yzu.drivertaipei.controller.DataUpdate;
import hackntu2015.edu.yzu.drivertaipei.controller.NavigationController;
import hackntu2015.edu.yzu.drivertaipei.ui.CardLayout;
import hackntu2015.edu.yzu.drivertaipei.utils.AutoResizeTextView;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;
import hackntu2015.edu.yzu.drivertaipei.utils.Utils;

public class MainActivity extends FragmentActivity {
    private String TAG = "MainActivity";

    private Context ctx;
    private LocationManager locationManager;
    private GoogleMap mMap;

    private ImageButton filter;
    private ImageButton menu;

    private LinearLayout detailBar;
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

    private CardLayout mCard;
    private Handler uiHandler = new Handler();
    DataUpdate dataHandler;
    private Marker mSelectMarker = null;

    private boolean insideScale = true;
    private boolean gasIsCheck = true;
    private boolean parkingLotIsCheck = true;
    private boolean constructIsCheck = true;
    private boolean carFlowIsCheck = true;

    List<NodeParkingLot> nodeParkingLot;
    List<NodeGas> nodeGas;
    List<NodeConstruct> nodeConstruct;
    List<NodeTraffic> nodeTraffic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;

        initMap();
        initUi();
        initActionBar();
        initLocation();

        updateData();

        mCard = new CardLayout(ctx,detailLinearLayout,mMap,categoryNavigation,categoryPayMent);
        categoryNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationController.navigate(ctx, new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), mSelectMarker.getPosition());
            }
        });

        dataHandler = new DataUpdate();
    }

    private void initMap(){
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mSelectMarker != null) {
                    mSelectMarker.remove();
                }
                mSelectMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.selectioncircle)).position(marker.getPosition()));
                if (gasData.get(marker) != null) {
                    showCard(gasData.get(marker));
                } else if (parkingLotData.get(marker) != null) {
                    showCard(parkingLotData.get(marker));
                } else if (constructData.get(marker) != null) {
                    showCard(constructData.get(marker));
                } else if (trafficData.get(marker) != null) {
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
                updateMarker(cameraPosition.target);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (detailBar.getVisibility() == View.VISIBLE)
                    removeCard();
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initLocation(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
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

    private void initUi(){
        detailBar = (LinearLayout)findViewById(R.id.detailLinearLayout);
        detailLinearLayout = (LinearLayout)findViewById(R.id.detailLinearLayout);
        categoryNavigation = (Button)findViewById(R.id.navigationButton);
        categoryPayMent = (Button)findViewById(R.id.parkingMoneyButton);

        filterLayout = (LinearLayout)findViewById(R.id.filter_layout);
        gasCheckBox = (CheckBox)findViewById(R.id.gas_checkBox);
        parkingLotCheckBox = (CheckBox)findViewById(R.id.parkingLot_checkBox);
        carFlowCheckBox = (CheckBox)findViewById(R.id.carFlow_checkBox);
        constructCheckBox = (CheckBox)findViewById(R.id.construct_checkBox);

        carFlowData = new HashMap<GroundOverlay, NodeCarFlow>();
        gasData = new HashMap<Marker, NodeGas>();
        parkingLotData = new HashMap<Marker, NodeParkingLot>();
        constructData = new HashMap<Marker, NodeConstruct>();
        trafficData = new HashMap<Marker, NodeTraffic>();
    }

    private void initActionBar(){
        menu = (ImageButton)findViewById(R.id.btn_menu);
        filter = (ImageButton)findViewById(R.id.btn_filter);

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
        ListView listView = (ListView) mMenu.findViewById(R.id.listView);
        listView.setAdapter(new MenuAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mMenu.toggleLeftDrawer();
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    default:
                        break;
                }
            }
        });

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
        filterImplement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataHandler.updateConstruct();
        dataHandler.updateTraffic();
    }

    @Override
    protected void onPause(){
        super.onPause();
        dataHandler.removeUpdate();
    }

    private void updateMarker(LatLng cameraPosition) {
        if (gasIsCheck) {
            for (Marker marker : gasData.keySet()) {
                marker.remove();
            }
            for (int i = 0; i < nodeGas.size(); i++) {
                if (Utils.getDistance(new LatLng(nodeGas.get(i).lat, nodeGas.get(i).lon), cameraPosition) < 1000) {
                    setGasInfo(nodeGas.get(i));
                }
            }
        } else {
            for (Marker marker : gasData.keySet()) {
                marker.setVisible(false);
            }
        }
        if (parkingLotIsCheck) {
            for (Marker marker : parkingLotData.keySet()) {
                marker.remove();
            }
            for (int i = 0; i < nodeParkingLot.size(); i++) {
                if (Utils.getDistance(new LatLng(nodeParkingLot.get(i).lat, nodeParkingLot.get(i).lon), cameraPosition) < 1000) {
                    setParkingInfo(nodeParkingLot.get(i));
                }
            }
        } else {
            for (Marker marker : parkingLotData.keySet()) {
                marker.setVisible(false);
            }
        }
        if (constructIsCheck) {
            for (Marker marker : constructData.keySet()) {
                marker.remove();
            }
            for (int i = 0; i < nodeConstruct.size(); i++) {
                if (Utils.getDistance(new LatLng(nodeConstruct.get(i).lat, nodeConstruct.get(i).lon), cameraPosition) < 1000) {
                    setConstructInfo(nodeConstruct.get(i));
                }
            }

            for (Marker marker : trafficData.keySet()) {
                marker.remove();
            }
            for (int i = 0; i < nodeTraffic.size(); i++) {
                if (Utils.getDistance(new LatLng(nodeTraffic.get(i).lat, nodeTraffic.get(i).lon), cameraPosition) < 1000) {
                    setTrafficInfo(nodeTraffic.get(i));
                }
            }
        } else {
            for (Marker marker : constructData.keySet()) {
                marker.setVisible(false);
            }
            for (Marker marker : trafficData.keySet()) {
                marker.setVisible(false);
            }
        } if (carFlowIsCheck) {
            mMap.setTrafficEnabled(true);
        } else {
            mMap.setTrafficEnabled(false);
        }
    }

    private void filterImplement(){
        gasCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gasIsCheck = isChecked;
                updateMarker(mMap.getCameraPosition().target);
            }
        });

        parkingLotCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                parkingLotIsCheck = isChecked;
                updateMarker(mMap.getCameraPosition().target);
            }
        });

        constructCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                constructIsCheck = isChecked;
                updateMarker(mMap.getCameraPosition().target);
            }
        });

        carFlowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                carFlowIsCheck = isChecked;
                updateMarker(mMap.getCameraPosition().target);
            }
        });
    }

    private void updateData(){
        DataManager.getInstance().setListener(new DataListener() {
            @Override
            public void onParkingLotDataDownloadComplete() {
                Log.i(TAG,"ParkingLotDataDownloadComplete");
                DataManager.getInstance().updateParkingLotData();
            }

            @Override
            public void onGasDataDownloadComplete() {
                Log.i(TAG,"GasDataDownloadComplete");
                DataManager.getInstance().updateGasData();
            }

            @Override
            public void onConstructDataDownloadComplete() {
                Log.i(TAG,"ConstructDataDownloadComplete");
                DataManager.getInstance().updateConstructData();
            }

            @Override
            public void onTrafficDataDownloadComplete() {
                Log.i(TAG,"TrafficDataDownloadComplete");
                DataManager.getInstance().updateTrafficData();
            }

            @Override
            public void onDataDownloadFailed(ErrorCode err, DataController.DataType dataType) {
                switch (dataType){
                    case construct:
                        Log.e(TAG,"construct download fail");
                        break;
                    case traffic:
                        Log.e(TAG,"traffic download fail");
                        break;
                    case gas:
                        Log.e(TAG,"gas download fail");
                        break;
                    case parkinglot:
                        Log.e(TAG,"parkinglot download fail");
                        break;
                }
            }

            @Override
            public void onGasDataUpdate(List<NodeGas> gasList) {
                nodeGas = gasList;
            }

            @Override
            public void onParkingLotDataUpdate(List<NodeParkingLot> parkingLotList) {
                nodeParkingLot = parkingLotList;
            }

            @Override
            public void onConstructDataUpdate(List<NodeConstruct> constructList) {
                nodeConstruct = constructList;
            }

            @Override
            public void onTrafficDataUpdate(List<NodeTraffic> trafficList) {
                nodeTraffic = trafficList;
            }
        });
        DataManager.getInstance().updateData();
    }

    private void removeCard(){
        mCard.removeCard();
        if(mSelectMarker != null) {
            mSelectMarker.remove();
        }
    }

    private void setGasInfo(NodeGas nodeGas){
        gasData.put(mCard.getGasMarker(nodeGas),nodeGas);
    }

    private void showCard(NodeGas nodeGas){
        mCard.showGasCard(nodeGas);
    }

    private void setParkingInfo(NodeParkingLot nodeParkingLot) {
        parkingLotData.put(mCard.getParkingLotMarker(nodeParkingLot),nodeParkingLot);
    }

    private void showCard(final NodeParkingLot nodeParkingLot){
        mCard.showParkingLotCard(nodeParkingLot);
        categoryPayMent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder payDialog = new AlertDialog.Builder(ctx);

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
                payDialog.setView(dialoglayout);
                final AlertDialog dialog = payDialog.show();
                dialog.getWindow().setLayout(Utils.getWindowWidth(getWindowManager()) * 4 / 5, Utils.getWindowHeight(getWindowManager()) * 4 / 5);
                AutoResizeTextView content = (AutoResizeTextView) dialoglayout.findViewById(R.id.dialog_content);
                content.setText(nodeParkingLot.payDes);
                Button closebtn = (Button) dialoglayout.findViewById(R.id.dialog_close);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setConstructInfo(NodeConstruct nodeConstruct) {
        constructData.put(mCard.getConstructMarker(nodeConstruct),nodeConstruct);

    }

    private void showCard(final NodeConstruct nodeConstruct){
        mCard.showConstructInfo(nodeConstruct);
    }

    private void setTrafficInfo(NodeTraffic nodeTraffic) {
        trafficData.put(mCard.getTrafficMarker(nodeTraffic), nodeTraffic);
    }

    private void showCard(final NodeTraffic nodeTraffic){
        mCard.showTrafficInfo(nodeTraffic);
    }
}