package com.example.uitestsample

import android.view.Menu
import android.view.View
import com.example.uitestsample.api.response.SinglePostResponse

interface MainActivityViewContract {

    fun createFloatMenu(id: Int, menu: Menu)

    fun updateMainContentText(text: String)

    fun handleOkButton(view: View)

    fun restCounter()

    fun handleSuccess(result: Array<SinglePostResponse>)

    fun handleError(message: String)
}
