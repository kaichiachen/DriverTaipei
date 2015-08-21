package hackntu2015.edu.yzu.drivertaipei.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by andy on 8/15/15.
 */
public class RequestClient {
    private static final String PARKINGLOT_URL = "http://drivertaipei.cloudapp.net/api/parking";
    private static final String CONSTRUCT_URL = "http://drivertaipei.cloudapp.net/api/construct";
    private static final String GAS_URL = "http://drivertaipei.cloudapp.net/api/gas";
    private static final String CARFLOW_URL ="http://drivertaipei.cloudapp.net/api/vd";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void requestParkingLot(AsyncHttpResponseHandler responseHandler){
        client.get(PARKINGLOT_URL,responseHandler);
    }

    public static void requestConstruct(AsyncHttpResponseHandler responseHandler){
        client.get(CONSTRUCT_URL,responseHandler);
    }

    public static void requestGas(AsyncHttpResponseHandler responseHandler){
        client.get(GAS_URL,responseHandler);
    }

    public static void requestCarFlow(AsyncHttpResponseHandler responseHandler){
        client.get(CARFLOW_URL,responseHandler);
    }
}
