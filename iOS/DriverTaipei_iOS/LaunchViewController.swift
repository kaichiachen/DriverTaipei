//
//  LaunchViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/23/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class LaunchViewController: BasicViewController {
    
    var gasDownloadSuccess = false
    var parkinglotDownloadSuccess = false
    var constructDownloadSuccess = false
    var trafficDownloadSuccess = false

    override func viewDidLoad() {
        super.viewDidLoad()
        DataController.getInstance().fetchAll()
    }
    
    override func shouldAutorotate() -> Bool {
        return false
    }
    
    override func supportedInterfaceOrientations() -> UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.Portrait
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let appDelegate:AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let slideMenuController = appDelegate.setSlideView(storyboard)
        appDelegate.window?.rootViewController = slideMenuController
    }
    
    override func gasFetched() {
        print("gas count: \(DataController.getInstance().dataStore?.getGasList().count)")
        gasDownloadSuccess = true
        self.performSegueWithIdentifier("mainSegue", sender: self)
    }
    
    override func parkingFetched() {
        print("park count: \(DataController.getInstance().dataStore?.getParkingLotList().count)")
        parkinglotDownloadSuccess = true
        self.performSegueWithIdentifier("mainSegue", sender: self)
    }
    
    override func constructFetched() {
        print("construct count: \(DataController.getInstance().dataStore?.getConstructList().count)")
        constructDownloadSuccess = true
        self.performSegueWithIdentifier("mainSegue", sender: self)
    }
    
    override func trafficFetched() {
        print("traffic count: \(DataController.getInstance().dataStore?.getTrafficList().count)")
        trafficDownloadSuccess = true
        self.performSegueWithIdentifier("mainSegue", sender: self)
    }
    
    override func failureFetch(type: DataType, error: String) {
        switch type {
        case .gas:
            print("GasError : \(error)")
        case .constructure:
            print("ConstructError : \(error)")
        case .parkinglot:
            print("ParkingLotError : \(error)")
        case .traffic:
            print("TrafficError : \(error)")
        }
    }
}
