package com.example.myapplication

import android.view.Menu
import android.view.View
import com.example.myapplication.api.response.SinglePostResponse

interface MainActivityViewContract {

    fun createFloatMenu(id: Int, menu: Menu)

    fun updateMainContentText(text: String)

    fun handleOkButton(view: View)

    fun restCounter()

    fun handleSuccess(result: Array<SinglePostResponse>)

    fun handleError(message: String)
}
