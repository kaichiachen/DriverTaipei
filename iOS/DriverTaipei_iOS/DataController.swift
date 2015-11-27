
import Foundation

class DataController{
    private static var dataManager:DataController?
    var dataSource:TraficDataSource?
    var dataStore:DataStore?
    
    static func getInstance() -> DataController {
        
        // prevent race condition
        objc_sync_enter(dataManager)
            if dataManager == nil {
                dataManager = DataController()
            }
        objc_sync_exit(dataManager)
        return dataManager!
    }
    
    init(){
        dataStore = DataStore()
    }
    
    func fetchAll(){
        fetchGas()
        fetchTraffic()
        fetchParkingLot()
        fetchConstruct()
    }
    
    func fetchGas(){
        GasRequest.fetch(){ response in
            dispatch_async(dispatch_get_main_queue()) {
                switch response {
                    case let .SuccessArrayCallback(data):
                        self.dataStore?.clearGas()
                        for nodeGasJSON in data {
                            self.dataStore?.addNodeGas(NodeGas(jo: nodeGasJSON as! NSDictionary))
                        }
                        self.dataSource?.gasFetched()
                    case .SuccessDictionaryCallback(_):
                        break
                    case let .Error(err):
                        self.dataSource?.failureFetch(.gas, error: err)
                }
            }
        }
    }
    
    func fetchParkingLot(){
        ParkingLotRequest.fetch(){ response in
            dispatch_async(dispatch_get_main_queue()) {
                switch response {
                case let .SuccessArrayCallback(data):
                    self.dataStore?.clearParkingLot()
                    for nodeParkingLotJSON in data {
                        self.dataStore?.addNodePark(NodeParkingLot(jo: nodeParkingLotJSON as! NSDictionary))
                    }
                    self.dataSource?.parkingFetched()
                case .SuccessDictionaryCallback(_):
                    break
                case let .Error(err):
                    self.dataSource?.failureFetch(DataType.parkinglot, error: err)
                }
            }
        }
    }
    
    func fetchTraffic(){
        TrafficRequest.fetch(){ response in
            dispatch_async(dispatch_get_main_queue()) {
                switch response {
                case let .SuccessArrayCallback(data):
                    self.dataStore?.clearTraffic()
                    for nodeTrafficJSON in data {
                        self.dataStore?.addNodeTraffic(NodeTraffic(jo: nodeTrafficJSON as! NSDictionary))
                    }
                    self.dataSource?.trafficFetched()
                case .SuccessDictionaryCallback(_):
                    break
                case let .Error(err):
                    self.dataSource?.failureFetch(DataType.traffic, error: err)
                }
            }
        }
    }
    
    func fetchConstruct(){
        ConstructRequest.fetch(){ response in
            dispatch_async(dispatch_get_main_queue()) {
                switch response {
                case let .SuccessArrayCallback(data):
                    self.dataStore?.clearConstruct()
                    for nodeConstructJSON in data {
                        self.dataStore?.addNodeConstruct(NodeConstruct(jo: nodeConstructJSON as! NSDictionary))
                    }
                    self.dataSource?.constructFetched()
                case .SuccessDictionaryCallback(_):
                    break
                case let .Error(err):
                    self.dataSource?.failureFetch(DataType.constructure, error: err)
                }
            }
        }
    }
}