package hackntu2015.edu.yzu.drivertaipei.network;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by andy on 8/15/15.
 */
public class RequestClient {
    private static final String PARKINGLOT_URL = "http://drivertaipei.cloudapp.net/api/parking";
    private static final String CONSTRUCT_URL = "http://drivertaipei.cloudapp.net/api/construct";
    private static final String GAS_URL = "http://drivertaipei.cloudapp.net/api/gas";
    private static final String CARFLOW_URL = "http://drivertaipei.cloudapp.net/api/vd";
    private static final String TRAFFIC_URL = "http://drivertaipei.cloudapp.net/api/pbs";
    private static AsyncHttpClient aclient = new AsyncHttpClient(true, 80, 443);
    private static AsyncHttpClient sclient = new SyncHttpClient(true, 80, 443);

    public static void requestParkingLot(AsyncHttpResponseHandler responseHandler){
        getClient().get(PARKINGLOT_URL, responseHandler);
    }

    public static void requestConstruct(AsyncHttpResponseHandler responseHandler){
        getClient().get(CONSTRUCT_URL, responseHandler);
    }

    public static void requestGas(AsyncHttpResponseHandler responseHandler){
        getClient().get(GAS_URL, responseHandler);
    }

    public static void requestCarFlow(AsyncHttpResponseHandler responseHandler){
        getClient().get(CARFLOW_URL, responseHandler);
    }

    public static void requestTraffic(AsyncHttpResponseHandler responseHandler){
        getClient().get(TRAFFIC_URL, responseHandler);
    }

    private static AsyncHttpClient getClient() {
        if (Looper.myLooper() == null) {
            sclient.setTimeout(30000);
            return sclient;
        }
        aclient.setTimeout(30000);
        return aclient;
    }
}
