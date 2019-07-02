package com.example.myapplication

import android.view.Menu
import android.view.View

interface MainActivityViewContract {

    fun createFloatMenu(id: Int, menu: Menu)

    fun updateMainContentText(text: String)

    fun handleOkButton(view: View)

    fun restCounter()
}
