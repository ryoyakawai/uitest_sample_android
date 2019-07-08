package com.example.uitestsample


import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import com.example.uitestsample.uitestutils.ScreenshotTakingRule
import com.example.uitestsample.uitestutils.UiTestUtils
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 26)
@LargeTest
class MainActivityInstrumentedTest {

    private val _packageName = "com.example.uitestsample"
    private var mUTs: UiTestUtils = UiTestUtils()
    private lateinit var mDevice: UiDevice

    @Rule
    @JvmField
    var cGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Before
    @Throws(Exception::class)
    fun setup() {
        this.mUTs.launchApp(_packageName)
        this.mDevice = this.mUTs.getDevice()
    }

    @Rule
    @JvmField
    val screenshotRule = ScreenshotTakingRule(this.mUTs)

    @After
    fun teardown() { }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(_packageName, appContext.packageName)
        this.mUTs.sleep("SHR")

        this.mUTs.removeSuccessScreenShots()
    }

    @Test
    fun checkTextHelloWorld() {
        mUTs.screenShot()
        onView(withId(R.id.main_content_text)).check(matches(withText(containsString("Hello World!"))))
        mUTs.log_d("useAppContext()")
        this.mUTs.sleep("SHR")

        this.mUTs.removeSuccessScreenShots()
    }

    @Test
    fun checkButtonIncrementFloating() {
        //
        // To check initial counter
        mUTs.screenShot()
        var actualCount = this.mUTs.getText(withId(R.id.main_content_text))
        this.mUTs.log_d("[Counter initial] 🍏 expected=[Hello World!!] actual=[$actualCount]")
        assertEquals("[Counter initial] 🍏", "Hello World!!", actualCount)

        //
        // To check whether increment button works properly
        val willTap = 5
        val incrementButton =  withId(R.id.increment_fab_text)

        for(i in 1..willTap) {
            // Tap increment button
            onView(incrementButton).perform(click())

            mUTs.allowPermissionsIfNeeded()

            mUTs.screenShot()
            actualCount = this.mUTs.getText(withId(R.id.main_content_text))
            this.mUTs.log_d("[Counter SEQ] 🍏🍎 expected=[$i] actual=[$actualCount]")
            assertEquals("[Counter SEQ] 🍏🍎", i.toString(), actualCount + "_")

            // Wait for snack bar disappears
            mUTs.screenShot()
            val snackBarTapped = allOf(withId(android.support.design.R.id.snackbar_text), withText("Tapped $i times."))
            waitForSnackbarDisappear(snackBarTapped)
            this.mUTs.sleep("SHR")
        }
        this.mUTs.sleep("SHR")

        //
        // To check whether reset counter button works properly
        mUTs.screenShot()
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        this.mUTs.sleep("SHR")
        val menuButton = allOf(
                withId(R.id.title), withText("Reset Counter"),
                this.mUTs.cAP(this.mUTs.cAP(withId(R.id.content), 0),0),
                isDisplayed())
        onView(menuButton).perform(click())
        this.mUTs.sleep("SHR")
        actualCount = this.mUTs.getText(withId(R.id.main_content_text))
        this.mUTs.log_d("[Counter Clear] 🍏🍎🍐 expected=[0] actual=[$actualCount]")
        assertEquals("[Counter Clear] 🍏🍎🍐", actualCount, "0")

        this.mUTs.removeSuccessScreenShots()
    }

    private fun waitForSnackbarDisappear(targetMatcher: Matcher<View>) {
        var doLoop = true
        while(doLoop) {
            try {
                onView(targetMatcher).check(matches(isDisplayed()))
                this.mUTs.sleep("SHR")
            } catch(e: NoMatchingViewException) {
                this.mUTs.log_e("This error is telling test runner that there are no snackbar on screen.")
                doLoop = false
            }
        }
    }

}

