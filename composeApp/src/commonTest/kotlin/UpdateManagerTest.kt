import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import com.clipsaver.quickreels.data.remote.models.*
import com.clipsaver.quickreels.AppConfig
import com.clipsaver.quickreels.utils.UpdateManager
import kotlin.test.assertNull

class FakeAppConfig(private val json: String) : AppConfig {
    override fun onConfig(callback: (Boolean, String) -> Unit) {
        callback(false, json)
    }
}

class UpdateManagerTest {

    private fun sampleJson(force_update_enabled : Boolean = false, optional_update_enabled : Boolean = false, maintenance_mode_enabled : Boolean = false) : String {
        return """{
          "force_update": {
            "enabled": $force_update_enabled,
            "version": "1.2.0",
            "title": "Update Required IOS",
            "message": "A new version of the app is available. Please update to continue using the app."
          },
          "optional_update": {
            "enabled": $optional_update_enabled,
            "version": "1.1.0",
            "title": "Update Available",
            "message": "A new version of the app is available. Would you like to update now?"
          },
          "others": {
            "base_url": "https://shahzaibdev.com"
          },
          "maintenance_mode": {
            "enabled": $maintenance_mode_enabled,
            "versions": [
              "1.0.0",
              "1.0.1"
            ],
            "title": "Server Maintenance",
            "message": "Our servers are currently down. Please try again later."
          }
        }
    """.trimIndent()
    }



    @Test
    fun testNormalUpdateFlow() {
        val fakeConfig = FakeAppConfig(sampleJson())
        val manager = UpdateManager(fakeConfig)

        var navigated = false
        var otherConfigCalled = false
        var shownDialog: BaseConfig? = null


        manager.checkConfig(
                currentVersion = "1.0.0",
                onNavigateToMain = {
                    navigated = true },
                onOtherConfig = { otherConfig ->
                    otherConfigCalled = true
                    assertEquals("https://shahzaibdev.com", otherConfig.baseUrl)
                },
                onShowDialog = { config ->
                    shownDialog = config
                }
        )

        // otherConfig should trigger
        assertTrue(otherConfigCalled)

        // Should  navigate to main for normal flow
        assertTrue(navigated)

        // A dialog must not  show
        assertNull(shownDialog)

    }


    @Test
    fun testForceUpdateFlow() {
        val fakeConfig = FakeAppConfig(sampleJson(force_update_enabled = true))
        val manager = UpdateManager(fakeConfig)

        var navigated = false
        var otherConfigCalled = false
        var shownDialog: BaseConfig? = null


        manager.checkConfig(
                currentVersion = "1.0.0",
                onNavigateToMain = {
                    navigated = true },
                onOtherConfig = { otherConfig ->
                    otherConfigCalled = true
                    assertEquals("https://shahzaibdev.com", otherConfig.baseUrl)
                },
                onShowDialog = { config ->
                    shownDialog = config
                }
        )

        // otherConfig should trigger
        assertTrue(otherConfigCalled)

        // Should NOT navigate to main because force update applies
        assertTrue(!navigated)

        // A dialog must be shown
        assertNotNull(shownDialog)
        assertEquals("Update Required IOS", shownDialog!!.title)
        assertEquals("A new version of the app is available. Please update to continue using the app.", shownDialog!!.message)
    }


    @Test
    fun testMaintainceUpdateFlow() {
        val fakeConfig = FakeAppConfig(sampleJson(maintenance_mode_enabled = true))
        val manager = UpdateManager(fakeConfig)

        var navigated = false
        var otherConfigCalled = false
        var shownDialog: BaseConfig? = null


        manager.checkConfig(
                currentVersion = "1.0.0",
                onNavigateToMain = {
                    navigated = true },
                onOtherConfig = { otherConfig ->
                    otherConfigCalled = true
                    assertEquals("https://shahzaibdev.com", otherConfig.baseUrl)
                },
                onShowDialog = { config ->
                    shownDialog = config
                }
        )

        // otherConfig should trigger
        assertTrue(otherConfigCalled)

        // Should NOT navigate to main because Maintaince update applies
        assertTrue(!navigated)

        // A dialog must be shown
        assertNotNull(shownDialog)
        assertEquals("Server Maintenance", shownDialog!!.title)
        assertEquals("Our servers are currently down. Please try again later.", shownDialog!!.message)
    }
}
