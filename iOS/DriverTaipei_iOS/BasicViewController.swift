//
//  BasicViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/23/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class BasicViewController: UIViewController, TraficDataSource {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        DataController.getInstance().dataSource = self
    }
    
    func gasFetched() {
        
    }
    
    func parkingFetched() {
        
    }
    
    func trafficFetched() {
        
    }
    
    func constructFetched() {
        
    }
    
    func failureFetch(type:DataType, error:String) {
        
    }

}
