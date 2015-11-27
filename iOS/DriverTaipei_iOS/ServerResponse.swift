//
//  ServerResponse.swift
//  HeyYZU
//
//  Created by Andy Chen on 10/20/15.
//  Copyright © 2015 Andy chen. All rights reserved.
//

import Foundation

enum ServerResponse {
    case SuccessArrayCallback(NSArray)
    case SuccessDictionaryCallback(NSDictionary)
    case Error(String)
}

enum ErrorCode:Int{
    case ERR_CONVERT = 0
    case ERR_JSON
    case ERR_WRONGURL
    case ERR_REQUEST
    
    func description() -> String {
        switch self {
        case .ERR_CONVERT:
            return "convertToArray Error"
        case .ERR_JSON:
            return "JSON Key Wrong!"
        case .ERR_WRONGURL:
            return "URL is Wrong!"
        case .ERR_REQUEST:
            return "REQUEST ERROR"
        }
    }
    
    func code() -> Int {
        switch self {
        case .ERR_CONVERT:
            return 0
        case .ERR_JSON:
            return 1
        case .ERR_WRONGURL:
            return 2
        case .ERR_REQUEST:
            return 3
        }
    }
}