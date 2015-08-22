package hackntu2015.edu.yzu.drivertaipei;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.controller.DataController;
import hackntu2015.edu.yzu.drivertaipei.controller.DataListener;
import hackntu2015.edu.yzu.drivertaipei.controller.DataManager;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;

/**
 * Created by andy on 8/18/15.
 */
public class LoadingActivity extends Activity {

    String TAG = "LoadingActivity";
    int time = 0;
    boolean isParkingLotDownloaded = false;
    boolean isConstructDownloaded = false;
    boolean isCarFLowDownloaded = false;
    boolean isGasDownloaded = false;
    boolean isTrafficDownloaded = false;
    int loadingCount = 0;
    Context ctx;

    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        time = 0;
        ctx = this;
        loadingBar = (ProgressBar)findViewById(R.id.loading_progress);
        loadingBar.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();

        Runnable loading = new Runnable(){

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                time++;
                Log.d(TAG,"timer: "+time);
                if (time > 2 && isGasDownloaded && isCarFLowDownloaded && isConstructDownloaded && isTrafficDownloaded && isParkingLotDownloaded) {
                    Log.i(TAG,"timer: "+time);
                    handler.removeCallbacks(this);
                    Intent i = new Intent(LoadingActivity.this, MainActivity.class);
                    ctx.startActivity(i);
                    finish();
                    return;
                } else if(time > 30){
                    handler.removeCallbacks(this);
                    AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                    alert.setMessage(R.string.network_error).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).show();
                    return;
                } else if(time>3){
                    loadingBar.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.post(loading);


        DataManager.getInstance().setListener(new DataListener() {

            @Override
            public void onParkingLotDataDownloadComplete() {
                loadingCount++;
                isParkingLotDownloaded = true;
                Log.i(TAG, "ParkingLog Data Download Complete");
            }

            @Override
            public void onGasDataDownloadComplete() {
                loadingCount++;
                isGasDownloaded = true;
                Log.i(TAG, "Gas Data Download Complete");
            }

            @Override
            public void onConstructDataDownloadComplete() {
                loadingCount++;
                isConstructDownloaded = true;
                Log.i(TAG, "Construct Data Download Complete");
            }

            @Override
            public void onCarFlowDataDownloadComplete() {
                loadingCount++;
                isCarFLowDownloaded = true;
                Log.i(TAG, "CarFlow Data Download Complete");
            }

            @Override
            public void onTrafficDataDownloadComplete() {
                loadingCount++;
                isTrafficDownloaded = true;
                Log.i(TAG, "Traffic Data Download Complete");
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataDownloadFailed(ErrorCode err, DataController.DataType dataType) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setMessage(R.string.data_error).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                }).show();
            }

            @Override
            public void onGasDataUpdate(List<NodeGas> gasList) {

            }

            @Override
            public void onParkingLotDataUpdate(List<NodeParkingLot> parkingLotList) {

            }

            @Override
            public void onConstructDataUpdate(List<NodeConstruct> constructList) {

            }

            @Override
            public void onCarFlowDataUpdate(List<NodeCarFlow> carFlowList) {

            }

            @Override
            public void onTrafficDataUpdate(List<NodeTraffic> trafficList) {

            }
        });
        DataManager.getInstance().downloadParkingLotData();
        DataManager.getInstance().downloadCarFlowData();
        DataManager.getInstance().downloadGasData();
        DataManager.getInstance().downloadConstructData();
        DataManager.getInstance().downloadTrafficData();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
