//
//  ParkingLotView.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/26/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit
import MapKit

class ParkingLotView: CardView {
    
    var nodeParkingLot:NodeParkingLot!
    
    init(nodeParkingLot:NodeParkingLot) {
        self.nodeParkingLot = nodeParkingLot
        if UIDevice.currentDevice().userInterfaceIdiom == .Phone {
            super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.8, 50))
        } else {
            super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.85, 50))
        }
        
        addnavigationButton()
        addDashBoardContent()
    }
    
    func addnavigationButton() {
        let navigationButton = UIButton()
        navigationButton.translatesAutoresizingMaskIntoConstraints = false
        navigationButton.setBackgroundImage(UIImage(named: "navigationbutton"), forState: UIControlState.Normal)
        navigationButton.sizeToFit()
        self.addConstraint(NSLayoutConstraint(item: navigationButton, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: self, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: -5))
        self.addConstraint(NSLayoutConstraint(item: navigationButton, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: self, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: -5))
        self.addConstraint(NSLayoutConstraint(item: navigationButton, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: self, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: 5))
        self.addConstraint(NSLayoutConstraint(item: navigationButton, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: navigationButton, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        
        self.addSubview(navigationButton)
        
        navigationButton.addTarget(self, action: "navigate:", forControlEvents: UIControlEvents.TouchUpInside)
        self.layoutIfNeeded()
    }
    
    func addDashBoardContent() {
        let parkingCarIcon = UIImageView(image: UIImage(named: "parkingdark"))
        parkingCarIcon.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingCarIcon, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Left, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingCarIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: parkingCarIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        verticalCenter(parkingCarIcon)
        dashBoard.addSubview(parkingCarIcon)
        
        let parkingCarCount = UILabel()
        if let carCount = nodeParkingLot.availableCar {
            parkingCarCount.text = "\(carCount)"
        } else {
            parkingCarCount.text = "\(0)"
        }
        parkingCarCount.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingCarCount, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: parkingCarIcon, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: 5))
        verticalCenter(parkingCarCount)
        dashBoard.addSubview(parkingCarCount)
        
        let parkingMotorIcon = UIImageView(image: UIImage(named: "parking_bike_dark"))
        parkingMotorIcon.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingMotorIcon, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: parkingCarCount, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingMotorIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: parkingMotorIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        verticalCenter(parkingMotorIcon)
        dashBoard.addSubview(parkingMotorIcon)
        
        let parkingMotorCount = UILabel()
        if let motorCount = nodeParkingLot.availableMotor {
            parkingMotorCount.text = "\(motorCount)"
        } else {
            parkingMotorCount.text = "\(0)"
        }
        parkingMotorCount.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: parkingMotorCount, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: parkingMotorIcon, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: 5))
        verticalCenter(parkingMotorCount)
        dashBoard.addSubview(parkingMotorCount)
        
        
        let statusLabel = UILabel()
        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        let emotionIcon = UIImageView()
        emotionIcon.translatesAutoresizingMaskIntoConstraints = false
        if nodeParkingLot.availableMotor > 0 || nodeParkingLot.availableCar > 0 {
            statusLabel.text = "尚有車位"
            emotionIcon.image = UIImage(named: "emoticon_happy_green")
            
        } else {
            statusLabel.text = "沒有車位"
            emotionIcon.image = UIImage(named: "emoticon_sad")
        }
        
        /*
        add status label
        */
        
        dashBoard.addConstraint(NSLayoutConstraint(item: statusLabel, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: -50))
        verticalCenter(statusLabel)
        dashBoard.addSubview(statusLabel)
        
        /*
        add emotion icon
        */
        
        verticalCenter(emotionIcon)
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: emotionIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: statusLabel, attribute: NSLayoutAttribute.Left, multiplier: 1, constant: -5))
        dashBoard.addSubview(emotionIcon)
        dashBoard.layoutIfNeeded()
        if emotionIcon.frame.origin.x < parkingMotorCount.frame.origin.x + parkingMotorCount.frame.size.width {
            emotionIcon.alpha = 0
        }
    }
    
    func navigate(sender: AnyObject) {
        let latitute:CLLocationDegrees =  nodeParkingLot.lat!
        let longitute:CLLocationDegrees =  nodeParkingLot.lon!
        let coordinates = CLLocationCoordinate2DMake(latitute, longitute)
        let placemark = MKPlacemark(coordinate: coordinates, addressDictionary: nil)
        let mapItem = MKMapItem(placemark: placemark)
        let currentLocationMapItem:MKMapItem = MKMapItem.mapItemForCurrentLocation()
        
        let launchOptions:NSDictionary = NSDictionary(object: MKLaunchOptionsDirectionsModeDriving, forKey: MKLaunchOptionsDirectionsModeKey)
        MKMapItem.openMapsWithItems([currentLocationMapItem,mapItem], launchOptions: launchOptions as? [String : AnyObject])
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}
