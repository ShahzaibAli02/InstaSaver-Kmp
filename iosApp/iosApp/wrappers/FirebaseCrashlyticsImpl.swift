//
//  FirebaseCrashlyticsImpl.swift
//  iosApp
//
//  Created by Shahzaib Ali on 16/12/2025.
//

import ComposeApp

import Foundation
import FirebaseCrashlytics
class FirebaseCrashlyticsImpl: AppEvent {
   

    func logEvent(message: String, type: AppEventType) async throws {
        Crashlytics.crashlytics().log(type == AppEventType.error ? "Errror" : "Info")
        let error = NSError(domain: "AppEvent",  code: 0, userInfo: [NSLocalizedDescriptionKey: message] )
        Crashlytics.crashlytics().record(error: error)
    }

  
}
