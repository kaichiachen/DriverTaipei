//
//  AboutViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/27/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class AboutViewController: UIViewController {
    
    @IBAction func leftMenu(sender: AnyObject) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let main = storyboard.instantiateViewControllerWithIdentifier("nav") as! UINavigationController
        self.slideMenuController()?.changeMainViewController(main, close: true)
    }
    
    var originHeight:CGFloat?
    
    @IBOutlet weak var itacDownConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var itacImage: UIImageView!
    
    @IBAction func aboutAction(sender: UIButton) {
        switch sender.tag {
        case 100:
            UIApplication.sharedApplication().openURL(NSURL(string: "http://erickson-makotoki.github.io/Image_Page/")!)
            break;
        case 101:
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let main = storyboard.instantiateViewControllerWithIdentifier("nav") as! UINavigationController
            self.slideMenuController()?.changeMainViewController(main, close: true)
        case 102:
            UIApplication.sharedApplication().openURL(NSURL(string: "http://erickson-makotoki.github.io/Image_Page/")!)
            default: break;
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        originHeight = itacDownConstraint.constant
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        let orientation = UIApplication.sharedApplication().statusBarOrientation
        if orientation == UIInterfaceOrientation.Portrait || UIDevice.currentDevice().userInterfaceIdiom == .Pad {
            itacImage.image = UIImage(named: "about_main")
            if orientation != UIInterfaceOrientation.Portrait {
                NSLayoutConstraint.activateConstraints([self.itacDownConstraint])
            } else {
                NSLayoutConstraint.deactivateConstraints([self.itacDownConstraint])
            }
        } else if orientation == UIInterfaceOrientation.LandscapeRight || orientation == UIInterfaceOrientation.LandscapeLeft {
            itacImage.image = UIImage(named: "about_main_landscape")
            itacDownConstraint.constant = 70
        }
    }
    
    override func willRotateToInterfaceOrientation(toInterfaceOrientation: UIInterfaceOrientation, duration: NSTimeInterval) {
        if UIInterfaceOrientation.Portrait == toInterfaceOrientation || UIDevice.currentDevice().userInterfaceIdiom == .Pad{
            itacImage.image = UIImage(named: "about_main")
            if toInterfaceOrientation != UIInterfaceOrientation.Portrait {
                NSLayoutConstraint.activateConstraints([self.itacDownConstraint])
            } else {
                NSLayoutConstraint.deactivateConstraints([self.itacDownConstraint])
            }
        } else {
            itacImage.image = UIImage(named: "about_main_landscape")
            itacDownConstraint.constant = 70
        }
    }
}
