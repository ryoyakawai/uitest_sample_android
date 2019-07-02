package com.ryoyakawai.uitestsample

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
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
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    var mUT = UiTestUtils()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.ryoyakawai.uitestsample", appContext.packageName)
        mUT.sleep("SHR")
    }

    @Test
    fun checkTextHelloWorld() {
        onView(withId(R.id.main_content_text)).check(matches(withText(containsString("Hello World!"))))
        mUT.sleep("SHR")
    }

    @Test
    fun checkButtonIncrementFloating() {
        //
        // To check initial counter
        val actualCount00 = mUT.getText(withId(R.id.main_content_text))
        mUT.log_d("[Counter initial] 🍏 expected=[Hello World!!] actual=[$actualCount00]")
        assertEquals("[Counter initial] 🍏", "Hello World!!", actualCount00)

        //
        // To check whether increment button works properly
        val willTap = 5
        for(i in 1..willTap) {
            val incrementButton =  withId(R.id.increment_fab_text)
            onView(incrementButton).check(matches(withText("+")))
            onView(incrementButton).perform(click())

            onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(containsString("Tapped"))))
                .check(matches(isDisplayed()))

            var counter00 = mUT.getText(withId(R.id.main_content_text))
            mUT.log_d("[Counter SEQ] 🍏🍎 expected=[$i] actual=[$counter00]")
            assertEquals("[Counter SEQ] 🍏🍎", i.toString(), counter00)

            // Wait for snack bar disappears
            val snackBarTapped = allOf(withId(android.support.design.R.id.snackbar_text), withText(containsString("Tapped")))
            waitForSnackbarDisappear(snackBarTapped)
            mUT.sleep("SHR")
        }
        mUT.sleep("SHR")

        //
        // To check whether reset counter button works properly
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        mUT.sleep("SHR")
        val menuButton = allOf(
                withId(R.id.title), withText("Reset Counter"),
                mUT.cAP(mUT.cAP(withId(R.id.content), 0),0),
                isDisplayed())
        onView(menuButton).perform(click())
        mUT.sleep("SHR")
        val actualCount01 = mUT.getText(withId(R.id.main_content_text))
        mUT.log_d("[Counter Clear] 🍏🍎🍐 expected=[0] actual=[$actualCount01]")
        assertEquals("[Counter Clear] 🍏🍎🍐", actualCount01, "0")
    }

    private fun waitForSnackbarDisappear(targetMatcher: Matcher<View>) {
        var doLoop = true
        while(doLoop) {
            try {
                onView(targetMatcher).check(matches(isDisplayed()))
                mUT.sleep("SHR")
            } catch(e: NoMatchingViewException) {
                doLoop = false
            }
        }
    }

}
