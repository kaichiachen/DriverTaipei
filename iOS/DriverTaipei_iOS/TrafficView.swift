//
//  ConstructView.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/26/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class TrafficView: CardView {
    
    var nodeTraffic:NodeTraffic!
    
    init(nodeTraffic:NodeTraffic) {
        self.nodeTraffic = nodeTraffic
        super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.90, 50))
        addDashBoardContent()
    }
    
    func addDashBoardContent() {
        let trafficStatus = UILabel()
        trafficStatus.text = nodeTraffic.status
        trafficStatus.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: trafficStatus, attribute: NSLayoutAttribute.CenterX, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.CenterX, multiplier: 1.0, constant: 0))
        verticalCenter(trafficStatus)
        trafficStatus.numberOfLines = 0
        trafficStatus.sizeToFit()
        dashBoard.addSubview(trafficStatus)
        
        
        let trafficIcon = UIImageView(image: UIImage(named: "attention_alert_warning_message_512"))
        trafficIcon.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: trafficIcon, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: trafficStatus, attribute: NSLayoutAttribute.Left, multiplier: 1.0, constant: -10))
        dashBoard.addConstraint(NSLayoutConstraint(item: trafficIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: trafficIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        verticalCenter(trafficIcon)
        dashBoard.addSubview(trafficIcon)
        dashBoard.layoutIfNeeded()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
