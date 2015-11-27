//
//  DataNode.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/23/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import Foundation

class NodeData {
    var lat:Double?
    var lon:Double?
    
    init(lat:Double?,lon:Double?) {
        self.lat = lat
        self.lon = lon
    }
}

class NodeGas:NodeData {
    var id:String?
    var name:String?
    var hasOil:Bool?
    var hasSelf:Bool?
    var hasGas:Bool?
    var serviceTime:String?
    
    init(jo: NSDictionary){
        super.init(lat: (jo["lat"] as? Double) ,lon: (jo["lng"] as? Double))
        id = jo["id"] as? String
        name = jo["name"] as? String
        hasOil = jo["hasOil"] as? Bool
        hasSelf = jo["hasSelf"] as? Bool
        hasGas = jo["hasGas"] as? Bool
        serviceTime = jo["serviceTime"] as? String
    }
}

class NodeParkingLot:NodeData {
    var id:String?
    var name:String?
    var availableCar:Int?
    var availableMotor:Int?
    var payDes:String?
    
    init(jo: NSDictionary) {
        super.init(lat: (jo["lat"] as? Double) ,lon: (jo["lng"] as? Double))
        id = jo["id"] as? String
        name = jo["name"] as? String
        availableCar = jo["availableCar"] as? Int
        availableMotor = jo["availableMotor"] as? Int
        payDes = jo["payDes"] as? String
    }
}

class NodeTraffic:NodeData {
    
    var id:String?
    var status:String?
    
    init(jo: NSDictionary) {
        let lat = jo["lat"] as? NSString
        let lon = jo["lng"] as? NSString
        super.init(lat: lat?.doubleValue ,lon: lon?.doubleValue)
        id = jo["id"] as? String
        status = jo["status"] as? String
    }
    
}

class NodeConstruct:NodeData {
    var id:String?
    var status:String?
    
    init(jo: NSDictionary) {
        super.init(lat: (jo["lat"] as? Double) ,lon: (jo["lng"] as? Double))
        id = jo["id"] as? String
        status = jo["status"] as? String
    }
}
