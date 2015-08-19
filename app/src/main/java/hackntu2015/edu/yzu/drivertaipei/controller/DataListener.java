package hackntu2015.edu.yzu.drivertaipei.controller;

import java.util.List;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeCarFlow;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.utils.ErrorCode;

/**
 * Created by andy on 8/15/15.
 */
public interface DataListener {

    public void onParkingLotDataDownloadComplete();
    public void onGasDataDownloadComplete();
    public void onConstructDataDownloadComplete();
    public void onCarFlowDataDownloadComplete();

    public void onDataDownloadFailed(ErrorCode err,DataController.DataType dataType);

    public void onGasDataUpdate(List<NodeGas> gasList);
    public void onParkingLotDataUpdate(List<NodeParkingLot> parkingLotList);
    public void onConstructDataUpdate(List<NodeConstruct> constructList);
    public void onCarFlowDataUpdate(List<NodeCarFlow> carFlowList);
}
