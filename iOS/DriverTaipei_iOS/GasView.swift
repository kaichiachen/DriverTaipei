//
//  GasView.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/24/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit
import MapKit

class GasView: CardView {
    
    var nodeGas:NodeGas!
    
    init(nodeGas:NodeGas) {
        self.nodeGas = nodeGas
        
        if UIDevice.currentDevice().userInterfaceIdiom == .Phone {
            super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.8, 50))
        } else {
            super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.85, 50))
        }
        addDashBoardContent()
        addnavigationButton()
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
        
        /*
            add gas icon
        */
        
        let imageView = UIImageView(image: UIImage(named: "petrolstation"))
        imageView.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: imageView, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Left, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: imageView, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: imageView, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: -10))
        dashBoard.addConstraint(NSLayoutConstraint(item: imageView, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: imageView, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        dashBoard.addSubview(imageView)
        
        /*
            add gas name
        */
        
        let gasName = UILabel()
        gasName.frame.size = CGSize(width: 50,height: self.frame.size.height)
        gasName.text = nodeGas.name
        gasName.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: gasName, attribute: NSLayoutAttribute.Left, relatedBy: NSLayoutRelation.Equal, toItem: imageView, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: 5))
        dashBoard.addConstraint(NSLayoutConstraint(item: gasName, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: super.dashBoard, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: gasName, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: -10))
        dashBoard.addConstraint(NSLayoutConstraint(item: gasName, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: nil, attribute: NSLayoutAttribute.NotAnAttribute, multiplier: 1.0, constant: 70))
        gasName.numberOfLines = 5
        gasName.sizeToFit()
        dashBoard.addSubview(gasName)
        
        let statusLabel = UILabel()
        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        let emotionIcon = UIImageView()
        emotionIcon.translatesAutoresizingMaskIntoConstraints = false
        if nodeGas.hasOil! {
            if nodeGas.hasSelf! {
                statusLabel.text = "自助"
                emotionIcon.image = UIImage(named: "emoticon_happy_green")
            } else {
                statusLabel.text = "非自助"
                emotionIcon.image = UIImage(named: "emoticon_happy")
            }
        } else {
            statusLabel.text = "非營業"
            emotionIcon.image = UIImage(named: "emoticon_sad")
        }
        imageView.sizeToFit()
        
        /*
            add status label
        */
        
        dashBoard.addConstraint(NSLayoutConstraint(item: statusLabel, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Right, multiplier: 1.0, constant: -50))
        dashBoard.addConstraint(NSLayoutConstraint(item: statusLabel, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: statusLabel, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: -10))
        dashBoard.addSubview(statusLabel)
        
        /*
            add emotion icon
        */
        
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: -10))
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: emotionIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        dashBoard.addConstraint(NSLayoutConstraint(item: emotionIcon, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: statusLabel, attribute: NSLayoutAttribute.Left, multiplier: 1, constant: -5))
        dashBoard.addSubview(emotionIcon)
        dashBoard.layoutIfNeeded()
        
    }
    
    func navigate(sender: AnyObject) {
        let latitute:CLLocationDegrees =  nodeGas.lat!
        let longitute:CLLocationDegrees =  nodeGas.lon!
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
