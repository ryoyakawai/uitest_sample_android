package com.example.myapplicationtest

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.myapplication", appContext.packageName)
        Thread.sleep(500)
    }

    @Test
    fun checkTexthelloWorld() {
        onView(withId(R.id.main_content_text)).check(matches(withText("Hello World!")))
        Thread.sleep(500)
    }

    @Test
    fun checkButtonEmailFloating() {
        onView(withId(R.id.email_fab_text)).check(matches(withText("OK")))
        onView(withId(R.id.email_fab)).perform(click())
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Replace with your own action")))
            .check(matches(isDisplayed()))
        Thread.sleep(500)
    }


}
