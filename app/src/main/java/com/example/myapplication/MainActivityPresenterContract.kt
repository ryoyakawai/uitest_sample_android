package com.example.myapplication

import com.example.myapplication.api.response.*
import io.reactivex.Single
import org.json.JSONObject

interface MainActivityPresenterContract {

    fun setView(view: MainActivityViewContract)

    fun getSimpleJsonSampleResponse(): JSONObject

    fun getJsonSampleResponse()//: Single<CommentsPostId1>

}
