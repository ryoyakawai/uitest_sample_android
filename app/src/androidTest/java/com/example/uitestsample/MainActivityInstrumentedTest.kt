package com.example.uitestsample

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.filters.LargeTest
import android.support.test.filters.SdkSuppress
import android.support.test.rule.GrantPermissionRule
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import androidx.test.uiautomator.UiDevice
import com.example.uitestsample.uitestutils.UiTestUtils
import android.app.PendingIntent.getActivity as getActivity1
import org.junit.Rule



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
    private lateinit var mUTs: UiTestUtils
    private lateinit var mDevice: UiDevice

    @Rule
    @JvmField
    var cGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Before
    fun setup() {
        this.mUTs = UiTestUtils()
        this.mUTs.startTest()
        this.mUTs.launchApp(_packageName)
        this.mDevice = this.mUTs.getDevice()
    }

    @After
    fun teardown() { }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        mUTs.screenShot()
        val appContext = getTargetContext()
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
            assertEquals("[Counter SEQ] 🍏🍎", i.toString(), actualCount)

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
