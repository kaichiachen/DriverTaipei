package hackntu2015.edu.yzu.drivertaipei.Node;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeGas {
    public String id;
    public String name;
    public String cityZone;
    public double lon;
    public double lat;
    public String stationName;
    public String address;
    public String tel;
    public String serviceTime;
    public boolean hasOil = false;
    public boolean hasSelf = false;
    public boolean hasGas = false;


    public NodeGas(JSONObject jo){
        try {
            id = jo.getString("id");
            name = jo.getString("name");
            lat = jo.getDouble("lat");
            lon = jo.getDouble("lng");
            hasOil = jo.getBoolean("hasOil");
            hasSelf = jo.getBoolean("hasSelf");
            hasGas = jo.getBoolean("hasGas");
            serviceTime = jo.getString("serviceTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public NodeGas(){
        id = "0";
        name = "ABC";
        lon = 0;
        lat = 0;
        name = "台塑的油";
        serviceTime = "00:00 ~ 24:00";
        hasOil = true;
        hasSelf = true;
    }
}
