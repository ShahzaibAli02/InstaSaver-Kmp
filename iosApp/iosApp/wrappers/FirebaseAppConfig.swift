//
//  FirebaseAppConfig.swift
//  iosApp
//
//  Created by Shahzaib Ali on 16/12/2025.
//

import ComposeApp

import FirebaseAppCheck
import Foundation

public class FirebaseAppCheck: NSObject, ComposeApp.AppCheck {

    public override init() {
        super.init()
//        AppCheck.setAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())

        // Make sure Firebase is configured before calling this
        // AppCheck provider should be set in AppDelegate or @main
        let providerFactory = YourAppCheckProviderFactory()
        AppCheck.setAppCheckProviderFactory(providerFactory)
    }

    /// Fetch the AppCheck token asynchronously
    public func getAppCheckToken() async throws -> String? {
        await withCheckedContinuation { continuation in
                let appCheck = AppCheck.appCheck()
                
                // Fetch token from Firebase AppCheck
                appCheck.token(forcingRefresh: false) { token, error in
                    if let error = error {
                        print("Failed to fetch AppCheck token: \(error)")
                        continuation.resume(returning: nil)
                        return
                    }
                    
                    guard let token = token?.token else {
                        continuation.resume(returning: nil)
                        return
                    }
                    
                    continuation.resume(returning: token)
                }
            }
    }
}

