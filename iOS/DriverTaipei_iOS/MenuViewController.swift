//
//  MenuViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/27/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit

class MenuViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var titleImage: UIImageView!
    
    @IBOutlet weak var tableMenu: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableMenu.delegate = self
        tableMenu.dataSource = self
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        tableMenu.frame.size.height = 150
        
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        if Utils.isMorning() {
            titleImage.image = UIImage(named: "menu_topimage_day")
        } else {
            titleImage.image = UIImage(named: "menu_topimage_night")
        }
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
        
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(tableView:UITableView, numberOfRowsInSection section:Int) -> Int
    {
        return 3
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        switch indexPath.row{
        case 0:
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let main = storyboard.instantiateViewControllerWithIdentifier("nav") as! UINavigationController
            self.slideMenuController()?.changeMainViewController(main, close: true)
        case 1:
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let setting = storyboard.instantiateViewControllerWithIdentifier("menunav") as! UINavigationController
            self.slideMenuController()?.changeMainViewController(setting, close: true)
        case 2:
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let about = storyboard.instantiateViewControllerWithIdentifier("aboutnav") as! UINavigationController
            self.slideMenuController()?.changeMainViewController(about, close: true)
        default: break
        }
    }
    
    let menuTitle = ["主畫面","設定","關於我們"]
    let menuIcon = ["ic_home","ic_settings","ic_people"]
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell:UITableViewCell=UITableViewCell(style: UITableViewCellStyle.Subtitle, reuseIdentifier: "mycell")
        cell.textLabel!.text=menuTitle[indexPath.row]
        cell.imageView?.image = UIImage(named: menuIcon[indexPath.row])
        cell.imageView?.frame.size = CGSize(width: 20,height: 20)
        cell.selectionStyle = .None
        return cell
    }
}
