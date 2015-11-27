package hackntu2015.edu.yzu.drivertaipei.Node;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeTraffic {

    public String id;
    public char region;
    public String status;
    public String srcDetail;
    public String areaName;
    public String direction;
    public String roadType;
    public String road;
    public String happenTime;
    public double lat;
    public double lon;
    public boolean isToday;

    public NodeTraffic(JSONObject jo){
        try {
            id = jo.getString("id");
            status = jo.getString("status");
            lat = jo.getDouble("lat");
            lon = jo.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
