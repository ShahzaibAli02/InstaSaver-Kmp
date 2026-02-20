import SwiftUI
import ComposeApp


@main
struct iOSApp: App {
    init() {
        UIView.appearance().overrideUserInterfaceStyle = .light
        
//        let controller = SharedController()
//        controller.callback = MySwiftHandler()

        // Later, when shared code triggers callback:
//        SharedController.shared.triggerCallback(value: "Hello from iOS")
       }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
WindowGroup {
            ContentView()
        }
    }
}
