
import Foundation

class Constant{
    
    static let ServerUrl = "http://drivertaipei-andychenspot.rhcloud.com/api"
    static let dashBoardViewTag = 1000
    static let paymentDialogTag = 1001
    static let agreementDialogTag = 1002
    
    static let gasCheckBoxTag = 2001
    static let parkingLotCheckBoxTag = 2002
    static let constructCheckBoxTag = 2003
    static let trafficCheckBoxTag = 2004
    
    static let gasCheckString = "gasCheckString"
    static let parkinglotCheckString = "parkinglotCheckString"
    static let constructCheckString = "constructCheckString"
    static let trafficCheckString = "trafficCheckString"
}

enum DataType {
    case gas, parkinglot, constructure, traffic
}
