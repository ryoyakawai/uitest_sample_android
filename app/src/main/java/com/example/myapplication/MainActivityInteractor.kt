package com.example.myapplication

import com.example.myapplication.api.APIClient
import com.example.myapplication.api.ApiConnection
import com.example.myapplication.api.response.*
import io.reactivex.Single
import org.json.JSONObject

class MainActivityInteractor {

    fun simpleSampleResponse() : JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("userId", "1")
        jsonObject.put("id", "2")
        jsonObject.put("success", "true")
        return jsonObject
    }

    fun sampleResponse() : Single<Array<SinglePostResponse>> {
        val mApiConnection: ApiConnection = APIClient.mApiConnection!!
        return mApiConnection.commentPostId1(1)
/*
        val jsonObject= JSONObject()
        jsonObject.put("userId", "1")
        jsonObject.put("id", "2")
        jsonObject.put("success", "true")
        return jsonObject
 */
    }
}