//
//  LoginRequest.swift
//  HeyYZU
//
//  Created by Andy Chen on 10/3/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import Foundation
import SwiftHTTP

class TrafficRequest{
    
    static let login_Url = Constant.ServerUrl + "/pbs"
    
    static func fetch(finished:((ServerResponse) -> Void)){
        do {
            let opt = try HTTP.GET(login_Url)
            opt.start { response in
                if let err = response.error {
                    finished(ServerResponse.Error("\(err.localizedDescription)"))
                }
                if let array = response.data.convertDataToNSArray(){
                    finished(ServerResponse.SuccessArrayCallback(array))
                } else {
                    finished(ServerResponse.Error(ErrorCode.ERR_CONVERT.description()))
                }
            }
            
        } catch let error {
            finished(ServerResponse.Error("\(error)"))
        }
    }
}
