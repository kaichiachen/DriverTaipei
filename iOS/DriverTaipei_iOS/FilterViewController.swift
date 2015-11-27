//
//  FilterViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/28/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit
import DLRadioButton

class FilterViewController: UIViewController {
    
    let userDefaults:NSUserDefaults = NSUserDefaults.standardUserDefaults()
    
    var mainUIViewController:MainViewController?
    
    @IBAction func checkBox(sender: DLRadioButton) {
        switch sender.tag {
        case Constant.gasCheckBoxTag:
            userDefaults.setObject(sender.selected,forKey:Constant.gasCheckString)
        case Constant.parkingLotCheckBoxTag:
            userDefaults.setObject(sender.selected,forKey:Constant.parkinglotCheckString)
        case Constant.constructCheckBoxTag:
            userDefaults.setObject(sender.selected,forKey:Constant.constructCheckString)
        case Constant.trafficCheckBoxTag:
            userDefaults.setObject(sender.selected,forKey:Constant.trafficCheckString)
        default: break
        }
        mainUIViewController?.addDataAnnotation()
    }
    
    @IBOutlet weak var gasCheckButton: DLRadioButton!
    
    @IBOutlet weak var constructCheckButton: DLRadioButton!
    
    @IBOutlet weak var parkingLotCheckButton: DLRadioButton!
    
    @IBOutlet weak var trafficCheckButton: DLRadioButton!

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        gasCheckButton.selected = userDefaults.objectForKey(Constant.gasCheckString) as? Bool ?? true
        constructCheckButton.selected = userDefaults.objectForKey(Constant.constructCheckString) as? Bool ?? true
        parkingLotCheckButton.selected = userDefaults.objectForKey(Constant.constructCheckString) as? Bool ?? true
        trafficCheckButton.selected = userDefaults.objectForKey(Constant.trafficCheckString) as? Bool ?? true
    }
    
    override var preferredContentSize:CGSize{
        get{
            return CGSize(width: 130, height: 160)
        }
        set{ super.preferredContentSize = newValue }
    }


}
