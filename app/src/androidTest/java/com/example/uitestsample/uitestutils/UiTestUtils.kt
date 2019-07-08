package com.example.uitestsample.uitestutils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsNull
import org.junit.rules.TestWatcher
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class UiTestUtils {

    lateinit var mDevice: UiDevice
    private val mLAUNCHTIMEOUT = 5000L

    private var screenShotCounter = 0
    private lateinit var screenShotDir: String
    private val screenShotDirFormat = "yyyyMMdd_HHmmssSSS"
    private lateinit var filePrefix: String

    private val msgTAG = "[MSG] <<📋🖌 UI TEST 🔎✔️>>"

    // ハッシュ値生成用文字列
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    init {
        this.updateFilePrefx()
        Log.d(msgTAG, "[Begin Test] 👉 ${this.filePrefix}")
    }

    fun updateFilePrefx() {
        this.filePrefix = randomString(10)
    }

    fun startTest() {
        this.updateFilePrefx()
        Log.d(msgTAG, "[Begin Test] 👉 ${this.filePrefix}")
    }

    fun getDevice(): UiDevice  {
        return mDevice
    }

    fun launchApp(packageName: String) {
        // Initialize Device
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Tap Home Button of Android
        mDevice.pressHome()

        // Wait 5secs after Launching
        val launcherPackage = mDevice.launcherPackageName
        Log.d(msgTAG, "packageName=[$launcherPackage]")
        MatcherAssert.assertThat(launcherPackage, IsNull.notNullValue())
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), mLAUNCHTIMEOUT)

        // Launch App
        //val context = InstrumentationRegistry.getContext()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager
            .getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        // Wait for defined duration as mLAUNCHTIMEOUT
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), mLAUNCHTIMEOUT)

        this.prepareScreenShot()
    }

    private fun prepareScreenShot() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(this.screenShotDirFormat)
        val formatted = current.format(formatter)
        val sdcard = Environment.getExternalStorageDirectory()
        this.screenShotDir = "$sdcard/uitest/$formatted-${randomString(10)}"
        File(this.screenShotDir).mkdirs()
        Log.d(msgTAG, "📷 saveDirectory=[${this.screenShotDir}]")
    }

    fun screenShot(type: String = ""): String {
        screenShotCounter += 1
        var picNumber = String.format("%06d", screenShotCounter)
        var path = "${this.screenShotDir}/Screenshot-${this.filePrefix}-$picNumber-$type.png"
        Log.d(msgTAG, "📷 filename=[$path]")
        var aPath = File(path)
        mDevice.takeScreenshot(aPath)
        while(!aPath.exists()) {
            this.sleep("SHR")
        }
        return aPath.toString()
    }

    fun removeSuccessScreenShots() {
        File("${this.screenShotDir}/").walkTopDown().forEach {
            if(it.name.contains("${this.filePrefix}-.*.png$".toRegex())) {
                //this.log_d(" [REMOVE] $it")
                it.delete()
                while (it.exists()) {
                    this.sleep("SHR")
                }
            }
        }
        File("${this.screenShotDir}/").delete()
/*
        var count = 0
        File("${this.screenShotDir}/").walkTopDown().forEach {
            if (it.getName().contains("${this.filePrefix}-.*success.*png$".toRegex())) {
                count += 1
            }
        }
        if(count == 0 ) {

        }
*/
    }

    fun allowPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermissions = this.mDevice.findObject(
                UiSelector()
                    .clickable(true).checkable(false).index(1))
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                } catch (e: UiObjectNotFoundException) {
                    log_d("[Allow Button Does Not Found]")
                }
            }
        }
    }

    fun randomString(length: Int): String {
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
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

class ScreenshotTakingRule(mUTs: UiTestUtils) : TestWatcher() {

    private var mUTs = mUTs

    override fun failed(e: Throwable?, description: org.junit.runner.Description?) {
        super.failed(e, description)
        val path = mUTs.screenShot("FAIL-$description")
        mUTs.log_d(">>> !!! TEST FAILED !!! <<< ScreenShot Taken method=[$description] filename=[$path]")
    }
}
