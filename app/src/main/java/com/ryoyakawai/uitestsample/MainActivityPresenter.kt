package com.ryoyakawai.uitestsample

import org.json.JSONObject
import android.util.Log
import com.ryoyakawai.uitestsample.api.response.SinglePostResponse
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
        val disposable: Disposable = mModel!!.getPostsById(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ResCommentsPostId: Array<SinglePostResponse> ->
                mView?.let {
                    for(item in ResCommentsPostId) {
                        //Log.d(">>>> JSON >>>>", item.toString())
                        val javaClass = item::class.java
                        javaClass.declaredFields.forEach { field ->
                            field.isAccessible = true
                            Log.d(" !!!! >>>> JSON >>>>", field.name + ":" + field.get(item))
                        }
                    }


                    it.handleSuccess(ResCommentsPostId)
                }
            }, { error: Throwable ->
                error.message.let {
                    if (it != null) {
                        mView?.handleError(it)
                    } else {
                        mView?.handleError("Something Went Wrong.")
                    }
                }
            })
            mDisposable.add(disposable)
    }

    override fun getSimpleJsonSampleResponse(): JSONObject {
        return mModel!!.simpleSampleResponse()
    }

}
