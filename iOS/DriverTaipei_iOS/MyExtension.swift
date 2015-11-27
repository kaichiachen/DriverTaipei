
import Foundation

extension NSData{
    func convertDataToNSArray() -> NSArray? {
        let data = self
        do{
            let json = try NSJSONSerialization.JSONObjectWithData(data, options: []) as? NSArray
            return json
        } catch _ {
            
        }
        return nil
    }
    
    func convertDataToNSDictionary() -> NSDictionary? {
        let data = self
        do{
            let json = try NSJSONSerialization.JSONObjectWithData(data, options: []) as? NSDictionary
            return json
        } catch _ {
            
        }
        return nil
    }
}
