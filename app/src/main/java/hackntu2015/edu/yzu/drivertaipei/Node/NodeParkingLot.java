package hackntu2015.edu.yzu.drivertaipei.Node;

import org.json.JSONObject;

/**
 * Created by andy on 8/15/15.
 */
public class NodeParkingLot {
    public int id;
    public int parkingid;
    public String area;
    public String name;
    public String summary;
    public String address;
    public String tel;
    public String pey;
    public String serviceTime;
    public int totalCar;
    public int totalMotor;
    public int totalBike;
    public int availableCar;
    public int availableMotor;
    public int getAvailableBike;
    public double lon;
    public double lat;

    public NodeParkingLot(JSONObject jo) {
    }
}
