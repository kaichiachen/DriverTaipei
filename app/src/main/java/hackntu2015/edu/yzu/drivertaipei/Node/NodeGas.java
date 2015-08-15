package hackntu2015.edu.yzu.drivertaipei.Node;

import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeGas {
    public int id;
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


    public NodeGas(JSONObject jo){

    }

    public NodeGas(){
        id = 0;
        name = "ABC";
        lon = 0;
        lat = 0;
        name = "台塑的油";
        serviceTime = "00:00 ~ 24:00";
        hasOil = false;
    }
}
