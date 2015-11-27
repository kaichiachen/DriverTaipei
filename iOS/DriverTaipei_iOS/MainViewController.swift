//
//  ViewController.swift
//  DriverTaipei_iOS
//
//  Created by Andy Chen on 10/23/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import UIKit
import MapKit
import ChameleonFramework
import CoreLocation

class MainViewController: BasicViewController, CLLocationManagerDelegate, MKMapViewDelegate,UIPopoverPresentationControllerDelegate {
    
    @IBOutlet var mapView: MKMapView!
    
    @IBAction func locationButton(sender: UIButton) {
        mapView.showsUserLocation = !mapView.showsUserLocation
        if mapView.showsUserLocation {
            sender.setBackgroundImage(UIImage(named: "ic_location_searching"), forState: .Normal)
        } else {
            sender.setBackgroundImage(UIImage(named: "ic_location_disabled"), forState: .Normal)
        }
    }
    
    @IBAction func openLeftSlide(sender: AnyObject) {
        self.slideMenuController()?.openLeft()
    }
    
    let userDefaults:NSUserDefaults = NSUserDefaults.standardUserDefaults()
    var locationManager:CLLocationManager!
    var selectedAnnotation:CustomAnnotation?

    override func viewDidLoad() {
        super.viewDidLoad()
        mapView.delegate = self
        locationManager = CLLocationManager()
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.startUpdatingLocation()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        mapView.removeAnnotations(mapView.annotations)
    }
    
    override func willRotateToInterfaceOrientation(toInterfaceOrientation: UIInterfaceOrientation, duration: NSTimeInterval) {
        for view in self.view.subviews {
            if view.tag == Constant.dashBoardViewTag {
                view.removeFromSuperview()
            }
        }
        closeDialog(UIButton())
        mapView.deselectAnnotation(selectedAnnotation, animated: true)
    }
    
    func addDataAnnotation(){
        let camera = mapView.camera.altitude < 7000
        let gasCheck = camera && userDefaults.objectForKey(Constant.gasCheckString) as? Bool ?? true
        let parkingLotCheck = camera && userDefaults.objectForKey(Constant.parkinglotCheckString) as? Bool ?? true
        let constructCheck = camera && userDefaults.objectForKey(Constant.constructCheckString) as? Bool ?? true
        let trafficCheck = camera && userDefaults.objectForKey(Constant.trafficCheckString) as? Bool ?? true
        
        var typeNum:Dictionary = [DataType.gas:0,DataType.parkinglot:0,DataType.constructure:0,DataType.traffic:0]
        for annotation in mapView.annotations {
            if let anno = annotation as? CustomAnnotation {
                switch anno.type! {
                case .gas:
                    typeNum[DataType.gas] = typeNum[DataType.gas]! + 1
                case .constructure:
                    typeNum[DataType.constructure] = typeNum[DataType.constructure]! + 1
                case .parkinglot:
                    typeNum[DataType.parkinglot] = typeNum[DataType.parkinglot]! + 1
                case .traffic:
                    typeNum[DataType.traffic] = typeNum[DataType.traffic]! + 1
                }
            }
        }
        
        if DataController.getInstance().dataStore?.getGasList().count > 0 && typeNum[DataType.gas] == 0 && gasCheck {
            addGasAnnotation()
        } else if typeNum[DataType.gas] > 0 && !gasCheck {
            for annotation in mapView.annotations {
                if let anno = annotation as? CustomAnnotation {
                    if anno.type == DataType.gas {
                        mapView.removeAnnotation(anno)
                    }
                }
            }
        }
        if DataController.getInstance().dataStore?.getParkingLotList().count > 0 && typeNum[DataType.parkinglot] == 0 && parkingLotCheck {
            addParkingLotAnnotation()
        } else if typeNum[DataType.parkinglot] > 0 && !parkingLotCheck {
            for annotation in mapView.annotations {
                if let anno = annotation as? CustomAnnotation {
                    if anno.type == DataType.parkinglot {
                        mapView.removeAnnotation(anno)
                    }
                }
            }
        }
        if DataController.getInstance().dataStore?.getConstructList().count > 0 && typeNum[DataType.constructure] == 0 && constructCheck {
            addConstructAnnotation()
        } else if typeNum[DataType.constructure] > 0 && !constructCheck {
            for annotation in mapView.annotations {
                if let anno = annotation as? CustomAnnotation {
                    if anno.type == DataType.constructure {
                        mapView.removeAnnotation(anno)
                    }
                }
            }
        }
        if DataController.getInstance().dataStore?.getTrafficList().count > 0 && typeNum[DataType.traffic] == 0 && trafficCheck {
            addTrafficAnnotation()
        } else if typeNum[DataType.traffic] > 0 && !trafficCheck {
            for annotation in mapView.annotations {
                if let anno = annotation as? CustomAnnotation {
                    if anno.type == DataType.traffic {
                        mapView.removeAnnotation(anno)
                    }
                }
            }
        }
    }
    
    func addGasAnnotation() {
        for nodeGas in (DataController.getInstance().dataStore?.getGasList())! {
            let info = CustomAnnotation(nodeData: nodeGas, type: DataType.gas)
            info.title = nodeGas.name
            mapView.addAnnotation(info)
        }
    }
    
    func addParkingLotAnnotation() {
        for nodeParkingLot in (DataController.getInstance().dataStore?.getParkingLotList())! {
            let info = CustomAnnotation(nodeData: nodeParkingLot, type: DataType.parkinglot)
            info.title = nodeParkingLot.name
            mapView.addAnnotation(info)
        }
    }
    
    func addConstructAnnotation() {
        for nodeConstruct in (DataController.getInstance().dataStore?.getConstructList())! {
            let info = CustomAnnotation(nodeData: nodeConstruct, type: DataType.constructure)
            info.title = nodeConstruct.status
            mapView.addAnnotation(info)
        }
    }
    
    func addTrafficAnnotation() {
        for nodeTraffic in (DataController.getInstance().dataStore?.getTrafficList())! {
            let info = CustomAnnotation(nodeData: nodeTraffic, type: DataType.traffic)
            info.title = nodeTraffic.status
            mapView.addAnnotation(info)
        }
    }
    
    func mapView(mapView: MKMapView,
        didSelectAnnotationView view: MKAnnotationView) {
        if let annotation = view.annotation as? CustomAnnotation {
            switch annotation.type! {
            case .gas:
                showUpDashBoard(GasView(nodeGas: annotation.nodeData as! NodeGas))
            case .parkinglot:
                showUpDashBoard(ParkingLotView(nodeParkingLot: annotation.nodeData as! NodeParkingLot))
            case .constructure:
                showUpDashBoard(ConstructView(nodeConstruct: annotation.nodeData as! NodeConstruct))
            case .traffic:
                showUpDashBoard(TrafficView(nodeTraffic: annotation.nodeData as! NodeTraffic))
            }
            selectedAnnotation = annotation
        }
    }
    
    func mapView(mapView: MKMapView, viewForAnnotation annotation: MKAnnotation) -> MKAnnotationView? {
        
        if !(annotation is CustomAnnotation) {
            return nil
        }
        
        let reuseId = "id"
        
        var anView = mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId)
        if anView == nil {
            anView = MKAnnotationView(annotation: annotation, reuseIdentifier: reuseId)
            anView!.canShowCallout = true
        }
        else {
            anView!.annotation = annotation
        }
        
        let cpa = annotation as! CustomAnnotation
        anView!.image = UIImage(named:cpa.imageName!)
        anView!.frame.size = CGSize(width: 30,height: 30)
        if cpa.type == DataType.parkinglot {
            let payment = PayButton()
            payment.frame.size = CGSize(width: 30,height: 30)
            payment.setBackgroundImage(UIImage(named: "parkingpayment"), forState: .Normal)
            if let paymentText = (cpa.nodeData as? NodeParkingLot)?.payDes {
                anView?.leftCalloutAccessoryView = payment
                payment.payment = paymentText
                 payment.addTarget(self, action: "showPayment:", forControlEvents: .TouchUpInside)
            }
        }
        return anView
        
    }
    
    func locationManager(manager: CLLocationManager, didUpdateToLocation newLocation: CLLocation, fromLocation oldLocation: CLLocation) {
        if mapView.showsUserLocation {
            let userCoordinate = CLLocationCoordinate2D(latitude: newLocation.coordinate.latitude, longitude: newLocation.coordinate.longitude)
            let eyeCoordinate = CLLocationCoordinate2D(latitude: newLocation.coordinate.latitude, longitude: newLocation.coordinate.longitude)
            let mapCamera = MKMapCamera(lookingAtCenterCoordinate: userCoordinate, fromEyeCoordinate: eyeCoordinate, eyeAltitude: 6000.0)
            mapView.setCamera(mapCamera, animated: false)
        }
    }
    
    
    func mapView(mapView: MKMapView, regionDidChangeAnimated animated: Bool) {
        if mapView.camera.altitude > 7000 {
            mapView.removeAnnotations(mapView.annotations)
        } else {
            addDataAnnotation()
        }
    }
    
    func showUpDashBoard(dashBoard: CardView){
        for view in self.view.subviews {
            if view.tag == Constant.dashBoardViewTag {
                view.removeFromSuperview()
            }
        }
        dashBoard.tag = Constant.dashBoardViewTag
        self.view.addSubview(dashBoard)
        UIView.animateWithDuration(0.5, animations: {
            dashBoard.alpha = 1
        })
    }
    
    func showPayment(sender:PayButton) {
        mapView.userInteractionEnabled = false
        let paymentView = UIView(frame: CGRectMake(Utils.getScreenWidth() * 0.1, Utils.getScreenHeight()*0.2, Utils.getScreenWidth()*0.8,Utils.getScreenHeight()*0.4))
        if UIDevice.currentDevice().userInterfaceIdiom == .Pad {
            paymentView.frame = CGRectMake(Utils.getScreenWidth() * 0.3, Utils.getScreenHeight()*0.3, Utils.getScreenWidth()*0.4,Utils.getScreenHeight()*0.2)
        }
        paymentView.tag = Constant.paymentDialogTag
        paymentView.backgroundColor = UIColor.whiteColor()
        paymentView.layer.masksToBounds = true
        paymentView.layer.cornerRadius = 10
        paymentView.alpha = 0
        
        let paymentLabel = UILabel(frame: CGRectMake(10,10,paymentView.frame.size.width - 20, paymentView.frame.size.height - 50))
        paymentView.addConstraint(NSLayoutConstraint(item: paymentLabel, attribute: .Bottom, relatedBy: .Equal, toItem: paymentView, attribute: .Bottom, multiplier: 1, constant: 50))
        paymentLabel.text = sender.payment
        paymentLabel.numberOfLines = 0
        paymentLabel.adjustsFontSizeToFitWidth = true
        //paymentLabel.sizeToFit()
        paymentView.addSubview(paymentLabel)
        
        let closeButton = UIButton()
        closeButton.translatesAutoresizingMaskIntoConstraints = false
        closeButton.setTitle("關閉", forState: .Normal)
        closeButton.setTitleColor(UIColor(red: 0, green: 0.5, blue: 1.0, alpha: 1.0), forState: .Normal)
        paymentView.addConstraint(NSLayoutConstraint(item: closeButton, attribute: .Right, relatedBy: .Equal, toItem: paymentView, attribute: .Right, multiplier: 1, constant: -10))
        paymentView.addConstraint(NSLayoutConstraint(item: closeButton, attribute: .Bottom, relatedBy: .Equal, toItem: paymentView, attribute: .Bottom, multiplier: 1, constant: -10))
        closeButton.addTarget(self, action: "closeDialog:", forControlEvents: .TouchUpInside)
        paymentView.addSubview(closeButton)
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: "closeDialog:"))
        self.view.addSubview(paymentView)
        paymentView.layoutIfNeeded()
        
        UIView.animateWithDuration(0.5, animations: {
            paymentView.alpha = 1
        })
    }
    
    func closeDialog(sender:UIButton) {
        for view in self.view.subviews {
            if view.tag == Constant.paymentDialogTag {
                UIView.animateWithDuration(0.5, animations: {
                    view.alpha = 0
                    },completion:{(value:Bool) in
                        view.removeFromSuperview()
                })
            }
        }
        mapView.userInteractionEnabled = true
    }
    
    override func gasFetched() {
        print("main gas")
        print("gas count: \(DataController.getInstance().dataStore?.getGasList().count)")
    }
    
    override func parkingFetched() {
        print("main parking")
        print("park count: \(DataController.getInstance().dataStore?.getParkingLotList().count)")
    }
    
    override func constructFetched() {
        print("main construct")
        print("construct count: \(DataController.getInstance().dataStore?.getConstructList().count)")
    }
    
    override func trafficFetched() {
        print("main traffic")
        print("traffic count: \(DataController.getInstance().dataStore?.getTrafficList().count)")
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if let identifier = segue.identifier{
            switch identifier{
            case "filterPopover":
                if let tvc = segue.destinationViewController as? FilterViewController {
                    if let ppc = tvc.popoverPresentationController{
                        ppc.delegate = self
                        tvc.mainUIViewController = self
                    }
                }
            default: break;
            }
        }

    }
    func adaptivePresentationStyleForPresentationController(controller: UIPresentationController) -> UIModalPresentationStyle {
        return UIModalPresentationStyle.None
    }

}

