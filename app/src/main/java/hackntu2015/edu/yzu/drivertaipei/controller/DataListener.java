package hackntu2015.edu.yzu.drivertaipei.controller;

import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;

/**
 * Created by andy on 8/15/15.
 */
public interface DataListener {

    public void onDataUpdate(List<NodeCarFlow> carFlowList, List<NodeParkingLot> parkingLotList, List<NodeTraffic> trafficList, List<NodeConstruct> constructsList, List<NodeGas> nodeGas);
    public void onDataConnectFailed(ErrorCode err);
}
