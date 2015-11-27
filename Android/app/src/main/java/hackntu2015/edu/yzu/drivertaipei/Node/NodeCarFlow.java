package hackntu2015.edu.yzu.drivertaipei.Node;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeCarFlow {

    public String id;
    public String name;
    public double avgSpeed;
    public LatLng centerLocation;
    public float width;
    public float height;
    public carFlowLevel level;

    public enum carFlowLevel{
        LOW, MEDIUM, FAST;
    }

    public NodeCarFlow(JSONObject jo){
        try {
            id = jo.getString("id");
            name = jo.getString("name");
            if(jo.getString("carLevel").equals("fast")){
                level = carFlowLevel.FAST;
            } else if(jo.getString("carLevel").equals("medium")){
                level = carFlowLevel.MEDIUM;
            } else if(jo.getString("carLevel").equals("low")){
                level = carFlowLevel.LOW;
            }
            centerLocation = new LatLng(jo.getDouble("lat"),jo.getDouble("lng"));
            height = (float) jo.getDouble("height");
            width = (float) jo.getDouble("width");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public NodeCarFlow(){
        centerLocation = new LatLng(20,-20);
        width = 300000;
        height = 300000;
        level = carFlowLevel.MEDIUM;
    }
}
