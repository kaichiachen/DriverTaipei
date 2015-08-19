package hackntu2015.edu.yzu.drivertaipei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
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
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        time = 0;
        ctx = this;
        final Handler handler = new Handler();

        Runnable loading = new Runnable(){

            @Override
            public void run() {
                time++;
                Log.e(TAG,"timer: "+time);
                if (time > 2 && isGasDownloaded && isParkingLotDownloaded && isConstructDownloaded) {
                    handler.removeCallbacks(this);
                    Intent i = new Intent(LoadingActivity.this, MainActivity.class);
                    ctx.startActivity(i);
                    finish();
                    return;
                } else if(time > 30){
                    AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                    alert.setMessage("網路連線不良").setPositiveButton("確定", new DialogInterface.OnClickListener() {
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
                handler.postDelayed(this,1000);
            }
        };
        handler.post(loading);


        DataManager.getInstance().setListener(new DataListener() {

            @Override
            public void onParkingLotDataDownloadComplete() {
                isParkingLotDownloaded = true;
            }

            @Override
            public void onGasDataDownloadComplete() {
                isGasDownloaded = true;
            }

            @Override
            public void onConstructDataDownloadComplete() {
                isConstructDownloaded = true;
            }

            @Override
            public void onCarFlowDataDownloadComplete() {
                isCarFLowDownloaded = true;
            }

            @Override
            public void onDataDownloadFailed(ErrorCode err, DataController.DataType dataType) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setMessage("資料連接有誤").setPositiveButton("確定", new DialogInterface.OnClickListener() {
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
        });
        DataManager.getInstance().downloadParkingLotData();
        //DataManager.getInstance().downloadCarFlowData();
        DataManager.getInstance().downloadGasData();
        DataManager.getInstance().downloadConstructData();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
