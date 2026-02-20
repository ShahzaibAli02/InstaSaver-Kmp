//
//  Untitled.swift
//  iosApp
//
//  Created by Shahzaib Ali on 08/12/2025.
//
import UIKit
import Firebase
import FirebaseMessaging
import UserNotifications
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func inject(){
        KoinHelperKt.doInitKoin(
                appCheck: FirebaseAppCheck(),
                appConfig: FirebaseRemoteConfigWrapper(),
                appEvent: FirebaseCrashlyticsImpl()
            )
    }
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {

        FirebaseApp.configure()
        
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            print("Permission granted:", granted)
        }
        UIApplication.shared.registerForRemoteNotifications()
//        application.registerForRemoteNotifications()
        Messaging.messaging().delegate = self
        inject()
        return true
    }

    // MARK: - FCM Token
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("FCM Token: \(fcmToken ?? "nil")")
        // You can send this token to your server
    }
    // APNS Registration Success
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
        let token = tokenParts.joined()
        print("APNs Device Token: \(token)")
        // Set APNS token for FCM
        Messaging.messaging().apnsToken = deviceToken
    }
    

    // MARK: - Foreground Notification
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler:
                                @escaping (UNNotificationPresentationOptions) -> Void) {
        print("userNotificationCenter")
        completionHandler([.banner, .sound])
    }

    // MARK: - Tap on Notification
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        print("Notification tapped")
        completionHandler()
    }
}

