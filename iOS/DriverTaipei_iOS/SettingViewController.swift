//
//  SettingViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/27/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class SettingViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    @IBAction func leftMenu(sender: AnyObject) {
        self.slideMenuController()?.openLeft()
    }
    

    @IBOutlet weak var settingTable: UITableView! {
        didSet {
            settingTable.delegate = self
            settingTable.dataSource = self
            settingTable.alwaysBounceVertical = false
        }
    }
    
    
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        //settingTable.frame.size.height = 150
    }
    
    override func willRotateToInterfaceOrientation(toInterfaceOrientation: UIInterfaceOrientation, duration: NSTimeInterval) {
        closeDialog(UIButton())
    }
    
    override func didRotateFromInterfaceOrientation(fromInterfaceOrientation: UIInterfaceOrientation) {
        showAgreement()
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        switch indexPath.row {
        case 0:
            UIApplication.sharedApplication().openURL(NSURL(string: UIApplicationOpenSettingsURLString)!)
        case 1:
            UIApplication.sharedApplication().openURL(NSURL(string: "http://erickson-makotoki.github.io/Image_Page/")!)
        case 2:
            showAgreement()
        default: break
        }
    }
    
    func tableView(tableView:UITableView, numberOfRowsInSection section:Int) -> Int
    {
        return 3
    }
    
    let menuTitle = ["改變位置使用權限","傳送意見","條款及隱私權"]
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell:UITableViewCell=UITableViewCell(style: UITableViewCellStyle.Subtitle, reuseIdentifier: "mycell")
        cell.textLabel!.text=menuTitle[indexPath.row]
        cell.selectionStyle = .None
        return cell
    }
    
    func showAgreement() {
        let agreementView = UIView(frame: CGRectMake(Utils.getScreenWidth() * 0.1, Utils.getScreenHeight()*0.15, Utils.getScreenWidth()*0.8,Utils.getScreenHeight()*0.7))
        agreementView.tag = Constant.agreementDialogTag
        agreementView.backgroundColor = UIColor.whiteColor()
        agreementView.layer.masksToBounds = true
        agreementView.layer.cornerRadius = 10
        agreementView.alpha = 0
        
        let agreementLabel = UILabel(frame: CGRectMake(10,10,agreementView.frame.size.width - 20, agreementView.frame.size.height - 50))
        agreementLabel.text = agreeText
        agreementLabel.numberOfLines = 0
        agreementLabel.sizeToFit()
        agreementView.addSubview(agreementLabel)
        
        let closeButton = UIButton()
        closeButton.translatesAutoresizingMaskIntoConstraints = false
        closeButton.setTitle("關閉", forState: .Normal)
        closeButton.setTitleColor(UIColor(red: 0, green: 0.5, blue: 1.0, alpha: 1.0), forState: .Normal)
        agreementView.addConstraint(NSLayoutConstraint(item: closeButton, attribute: .Right, relatedBy: .Equal, toItem: agreementView, attribute: .Right, multiplier: 1, constant: -10))
        agreementView.addConstraint(NSLayoutConstraint(item: closeButton, attribute: .Bottom, relatedBy: .Equal, toItem: agreementView, attribute: .Bottom, multiplier: 1, constant: -10))
        closeButton.addTarget(self, action: "closeDialog:", forControlEvents: .TouchUpInside)
        agreementView.addSubview(closeButton)
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: "closeDialog:"))
        self.view.addSubview(agreementView)
        agreementView.layoutIfNeeded()
        
        UIView.animateWithDuration(0.5, animations: {
            agreementView.alpha = 1
        })
    }
    
    func closeDialog(sender:UIButton) {
        for view in self.view.subviews {
            if view.tag == Constant.agreementDialogTag {
                UIView.animateWithDuration(0.5, animations: {
                    view.alpha = 0
                    },completion:{(value:Bool) in
                        view.removeFromSuperview()
                })
            }
        }
    }

    
    let agreeText = "本APP是以即時更新資料的方式運作，而一切資料均屬第三方提供，並非本網站提供，用戶應自行判斷內容之真實性。\n" +
        "對所有資料的真實性、完整性及可靠性等，本APP不負任何法律責任。\n" +
        "由於本APP受到「即時更新」運作方式所規限，故不能完全監察所有資料可靠性，若使用者發現有留言出現問題，請聯絡我們。\n" +
    "本APP保留一切法律權利。"
}
