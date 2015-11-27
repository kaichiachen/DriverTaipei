//
//  ConstructView.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/26/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class ConstructView: CardView {
    
    var nodeConstruct:NodeConstruct!
    
    init(nodeConstruct:NodeConstruct) {
        self.nodeConstruct = nodeConstruct
        super.init(dashBoardFrame: CGRectMake(Utils.getScreenWidth() * 0.05, 15, Utils.getScreenWidth() * 0.90, 50))
        addDashBoardContent()
    }
    
    func addDashBoardContent() {
        let constructionStatus = UILabel()
        constructionStatus.text = nodeConstruct.status
        constructionStatus.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: constructionStatus, attribute: NSLayoutAttribute.CenterX, relatedBy: NSLayoutRelation.Equal, toItem: dashBoard, attribute: NSLayoutAttribute.CenterX, multiplier: 1.0, constant: 0))
        verticalCenter(constructionStatus)
        constructionStatus.numberOfLines = 0
        constructionStatus.sizeToFit()
        dashBoard.addSubview(constructionStatus)
        
        
        let constructionIcon = UIImageView(image: UIImage(named: "constructionsite"))
        constructionIcon.translatesAutoresizingMaskIntoConstraints = false
        dashBoard.addConstraint(NSLayoutConstraint(item: constructionIcon, attribute: NSLayoutAttribute.Right, relatedBy: NSLayoutRelation.Equal, toItem: constructionStatus, attribute: NSLayoutAttribute.Left, multiplier: 1.0, constant: -10))
        dashBoard.addConstraint(NSLayoutConstraint(item: constructionIcon, attribute: NSLayoutAttribute.Width, relatedBy: NSLayoutRelation.Equal, toItem: constructionIcon, attribute: NSLayoutAttribute.Height, multiplier: 1.0, constant: 0))
        verticalCenter(constructionIcon)
        dashBoard.addSubview(constructionIcon)
        dashBoard.layoutIfNeeded()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
