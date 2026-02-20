//
//  FirebaseRemoteConfigWrapper.swift
//  iosApp
//
//  Created by Shahzaib Ali on 08/12/2025.
//

import Foundation
import FirebaseRemoteConfig
import ComposeApp

public class FirebaseRemoteConfigWrapper: NSObject , AppConfig {
    
    
   
    private let remoteConfig: RemoteConfig

    public override init() {
        remoteConfig = RemoteConfig.remoteConfig()
        super.init()

        let settings = RemoteConfigSettings()
        settings.minimumFetchInterval = 60
        remoteConfig.configSettings = settings
    }

    public func fetchAndActivate(completion: @escaping (Bool, Error?) -> Void) {
        remoteConfig.fetchAndActivate { status, error in
            if let error = error {
                completion(false, error)
            } else {
                completion(status != .error, nil)
            }
        }
    }

    public func getString(_ key: String) -> String {
        return remoteConfig[key].stringValue ?? ""
    }
    
    public func onConfig(onResponse: @escaping (KotlinBoolean, String) -> Void) {
        fetchAndActivate {[weak self] isSuccess, _ in
            if(!isSuccess){
                onResponse(true,"")
                return
            }
            let config = self?.getString("app_config")
            if(config == nil){
                onResponse(true,"")
            }
            else {
                onResponse(false,config!)
            }
        }
    }

}
