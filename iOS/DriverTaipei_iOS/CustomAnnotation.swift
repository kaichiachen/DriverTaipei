//
//  CustomAnnotation.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/24/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import MapKit

class CustomAnnotation: MKPointAnnotation {
    var imageName:String?
    var type:DataType?
    var nodeData:NodeData?
    
    init(nodeData:NodeData, type:DataType){
        super.init()
        
        self.nodeData = nodeData
        self.type = type
        
        coordinate = CLLocationCoordinate2D(latitude: self.nodeData!.lat! ,longitude: self.nodeData!.lon!)
        if let myType = self.type {
            switch myType {
            case .gas:
                self.imageName = "marker_petrolstation"
            case .parkinglot:
                self.imageName = "marker_parkinglot"
            case .constructure:
                self.imageName = "marker_constructionsite"
            case .traffic:
                self.imageName = "marker_warning"
            }
        }
        
    }
}