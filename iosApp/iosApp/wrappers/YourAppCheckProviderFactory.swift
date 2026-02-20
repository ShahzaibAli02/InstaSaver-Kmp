//
//  YourAppCheckProviderFactory.swift
//  iosApp
//
//  Created by Shahzaib Ali on 16/12/2025.
//

import ObjectiveC
import Firebase
class YourAppCheckProviderFactory: NSObject, AppCheckProviderFactory {
  func createProvider(with app: FirebaseApp) -> AppCheckProvider? {
    if #available(iOS 14.0, *) {
      return AppAttestProvider(app: app)
    } else {
      return DeviceCheckProvider(app: app)
    }
  }
}
