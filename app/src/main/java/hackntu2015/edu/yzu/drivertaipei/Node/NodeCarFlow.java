package hackntu2015.edu.yzu.drivertaipei.Node;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeCarFlow {

    public int id;
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

    }

    public NodeCarFlow(){
        centerLocation = new LatLng(20,-20);
        width = 300000;
        height = 300000;
        level = carFlowLevel.MEDIUM;
    }
}
