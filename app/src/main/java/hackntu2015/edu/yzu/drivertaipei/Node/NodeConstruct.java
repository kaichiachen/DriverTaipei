package hackntu2015.edu.yzu.drivertaipei.Node;

import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeConstruct {

    public int id;
    public double lat;
    public double lon;
    public String status;
    public String completeDate;
    public String startDate;
    public boolean isToday;

    public NodeConstruct(JSONObject jo){

    }

    public NodeConstruct(){
        status = "道路整修";
        isToday = false;
        startDate = "8/19";
        completeDate = "8/25";
        lat = -20;
        lon = 20;
    }

}
