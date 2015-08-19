package hackntu2015.edu.yzu.drivertaipei.Node;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andy on 8/15/15.
 */
public class NodeConstruct {

    public String id;
    public double lat;
    public double lon;
    public String status;
    public String completeDate;
    public String startDate;
    public boolean isToday;

    public NodeConstruct(JSONObject jo){
        try {
            id = jo.getString("id");
            status = jo.getString("status");
            lat = jo.getDouble("lat");
            lon = jo.getDouble("lng");
            startDate = jo.getString("startDay");
            completeDate = jo.getString("completeDay");
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String todayStr = format.format(today);
            try {
                today = format.parse(todayStr);
                Date oldday = format.parse(startDate);
                if((today.getTime()-oldday.getTime())<=0){
                    Log.e("XXX",""+today.getTime());
                    Log.e("XXX1",""+oldday.getTime());
                    isToday = true;
                } else {
                    isToday = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
