//
//  YourSimpleAppCheckProviderFactory.swift
//  iosApp
//
//  Created by Shahzaib Ali on 16/12/2025.
//

import ObjectiveC
import Firebase
class YourSimpleAppCheckProviderFactory: NSObject, AppCheckProviderFactory {
  func createProvider(with app: FirebaseApp) -> AppCheckProvider? {
    return AppAttestProvider(app: app)
  }
}
