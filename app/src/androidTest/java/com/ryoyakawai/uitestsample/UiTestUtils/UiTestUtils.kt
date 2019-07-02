package com.ryoyakawai.uitestsample

import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import kotlin.random.Random

class UiTestUtils {

    private val msgTAG = "[MSG] <<📋🖌 UI TEST 🔎✔️>>"

    // ハッシュ値生成用文字列
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    init {
        log_d("[Begin Test] 👉 " + randomString(10))
    }

    fun randomString(length: Int): String {
        return (1..length)
            .map { i -> Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun log_e(message: String) {
        Log.d(msgTAG, message)
    }

    fun log_d(message: String) {
        Log.d(msgTAG, message)
    }

    fun sleep(type: String) {
        var time = 1000
        when(type) {
            "LNG" -> time = 2500
            "NOR" -> time = 1000
            "SHR" -> time = 800
            "VSHR" -> time = 300
        }
        Thread.sleep(time.toLong())

        ViewActions.closeSoftKeyboard()
    }

    // childAtPosition()
    fun cAP(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    // get and return text
    fun getText(matcher: Matcher<View>): String {
        var stringHolder = emptyArray<String>()
        Espresso.onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "getting text from a TextView"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView //Save, because of check in getConstraints()
                stringHolder = arrayOf(tv.text.toString())
            }
        })
        return stringHolder[0]
    }

}