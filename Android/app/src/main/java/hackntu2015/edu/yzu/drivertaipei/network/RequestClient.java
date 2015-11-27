package hackntu2015.edu.yzu.drivertaipei.network;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by andy on 8/15/15.e
 */
public class RequestClient {
    private static final String SERVER_URL = "http://drivertaipei-andychenspot.rhcloud.com/api";
    private static final String PARKINGLOT_URL = SERVER_URL + "/parking";
    private static final String CONSTRUCT_URL = SERVER_URL + "/construct";
    private static final String GAS_URL = SERVER_URL + "/gas";
    private static final String TRAFFIC_URL = SERVER_URL + "/pbs";
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

    public static void requestTraffic(AsyncHttpResponseHandler responseHandler){
        getClient().get(TRAFFIC_URL, responseHandler);
    }

    private static AsyncHttpClient getClient() {
        if (Looper.myLooper() == null) {
            sclient.setTimeout(300000);
            return sclient;
        }
        aclient.setTimeout(300000);
        return aclient;
    }
}
