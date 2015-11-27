//
//  CardView.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/24/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class CardView: UIView {
    
    var dashBoard:UIView!
    
    init(dashBoardFrame:CGRect) {
        super.init(frame: CGRectMake(0, Utils.getScreenHeight() - 100.0, Utils.getScreenWidth(), 80))
        
        dashBoard = UIView(frame: dashBoardFrame)
        dashBoard.backgroundColor = UIColor(red: 0.92, green: 0.92, blue: 0.92, alpha: 0.9)
        dashBoard.layer.masksToBounds = true
        dashBoard.layer.cornerRadius = 10
        self.addSubview(dashBoard)
        self.alpha = 0
    }
    
    func verticalCenter(view:UIView) {
        dashBoard.addConstraint(NSLayoutConstraint(item: view, attribute: NSLayoutAttribute.Top, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Top, multiplier: 1.0, constant: 10))
        dashBoard.addConstraint(NSLayoutConstraint(item: view, attribute: NSLayoutAttribute.Bottom, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.Bottom, multiplier: 1.0, constant: -10))
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
