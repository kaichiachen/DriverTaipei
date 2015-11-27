
import Foundation
import UIKit

class Utils {
    
    static func getScreenHeight() -> CGFloat {
        return UIScreen.mainScreen().bounds.height
    }
    
    static func getScreenWidth() -> CGFloat {
        return UIScreen.mainScreen().bounds.width
    }
    
    static func hasNetwork() -> Bool {
        let statusReach: Reachability = Reachability.reachabilityForInternetConnection()
        let networksStatus: NetworkStatus = statusReach.currentReachabilityStatus()
        if networksStatus.rawValue == 0 { // 0(no internet), 1(wifi), 2(3G 4G)
            return false
        } else {
            return true
        }
    }
    
    static func isMorning() -> Bool {
        let nowDate = NSDate()
        let formatter = NSDateFormatter()
        formatter.dateFormat = "HH"
        let dateString = formatter.stringFromDate(nowDate)
        let hour = (dateString as NSString).doubleValue
        if hour > 6 && hour < 18 {
            return true
        } else {
            return false
        }
    }
}
