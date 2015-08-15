package hackntu2015.edu.yzu.drivertaipei.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by andy on 8/15/15.
 */
public class RequestClient {
    private static final String BASE_URL = "http://api.adserver.vm5apis.com/v3/trial";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void request(AsyncHttpResponseHandler responseHandler){
        client.get(BASE_URL,responseHandler);
    }
}
