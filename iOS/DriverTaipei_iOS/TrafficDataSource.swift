
import Foundation

protocol TraficDataSource {
    func gasFetched()
    func parkingFetched()
    func trafficFetched()
    func constructFetched()
    func failureFetch(type:DataType, error:String)
}