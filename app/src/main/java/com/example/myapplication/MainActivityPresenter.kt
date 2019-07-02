package com.example.myapplication

import com.example.myapplication.api.response.*
import io.reactivex.Single
import org.json.JSONObject
import android.util.Log
import com.example.myapplication.api.response.SinglePostResponse
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class MainActivityPresenter : MainActivityPresenterContract {

    private var mView: MainActivityViewContract? = null
    private var mModel: MainActivityInteractor? = null
    private val mDisposable = CompositeDisposable()

    init {
        mModel = MainActivityInteractor()
    }

    override fun setView(view: MainActivityViewContract) {
        mView = view
    }

    override fun getJsonSampleResponse() {
        val disposable: Disposable = mModel!!.sampleResponse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ResCommentsPostId1: Array<SinglePostResponse> ->
                mView?.let {
                    for(item in ResCommentsPostId1) {
                        Log.d(">>>> JSON >>>>", item.postId.toString())
                        Log.d(">>>> JSON >>>>", item.id.toString())
                        Log.d(">>>> JSON >>>>", item.name)
                        Log.d(">>>> JSON >>>>", item.email)
                        Log.d(">>>> JSON >>>>", item.body)
                    }
                }
            }, { _: Throwable ->
                mView?.let {
                    Log.e(">>>> JSON >>>>", "ERROR")
                }
            })
            mDisposable.add(disposable)
    }

    override fun getSimpleJsonSampleResponse(): JSONObject {
        return mModel!!.simpleSampleResponse()
    }

}