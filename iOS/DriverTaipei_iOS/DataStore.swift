//
//  DataStore.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/23/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import Foundation

class DataStore {
    private var gasList:[NodeGas]!
    private var parkingList:[NodeParkingLot]!
    private var constructList:[NodeConstruct]!
    private var trafficList:[NodeTraffic]!
    
    init(){
        gasList = [NodeGas]()
        parkingList = [NodeParkingLot]()
        constructList = [NodeConstruct]()
        trafficList = [NodeTraffic]()
    }
    
    func clearGas() {
        gasList.removeAll()
    }
    
    func addNodeGas(nodeGas:NodeGas) {
        gasList.append(nodeGas)
    }
    
    func getGasList() -> [NodeGas] {
        return gasList
    }
    
    func clearParkingLot() {
        parkingList.removeAll()
    }
    
    func addNodePark(nodePark:NodeParkingLot) {
        parkingList.append(nodePark)
    }
    
    func getParkingLotList() -> [NodeParkingLot] {
        return parkingList
    }
    
    func clearConstruct() {
        constructList.removeAll()
    }
    
    func addNodeConstruct(nodeConstruct:NodeConstruct) {
        constructList.append(nodeConstruct)
    }
    
    func getConstructList() -> [NodeConstruct] {
        return constructList
    }
    
    func clearTraffic() {
        trafficList.removeAll()
    }
    
    func addNodeTraffic(nodeTraffic:NodeTraffic) {
        trafficList.append(nodeTraffic)
    }
    
    func getTrafficList() -> [NodeTraffic] {
        return trafficList
    }
}
