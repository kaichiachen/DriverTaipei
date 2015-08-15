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
    public String payDes;
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

    public NodeParkingLot(){
        id = 1;
        availableCar = 0;
        availableMotor = 120;
        lat = 30;
        lon = 30;
        payDes = "計時：20元/時(8-18)，10元/時(18-8)。每日(20-8)最高收費50元，全程以半小時計費。月租：全日3,600元，日間2,400元(7-19)，夜間1,200元(限週一至週五19-8，及星期六、日與行政機關放假之紀念日、民俗日)";
    }
}
