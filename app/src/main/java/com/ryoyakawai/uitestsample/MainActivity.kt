package com.ryoyakawai.uitestsample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.util.Log
import com.ryoyakawai.uitestsample.api.response.SinglePostResponse


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityViewContract {

    private var mPresenter: MainActivityPresenterContract? = null
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter = MainActivityPresenter()
        mPresenter!!.setView(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        updateMainContentText("Hello World!!")

        email_fab.setOnClickListener { view ->
            counter += 1
            updateMainContentText("$counter")

            handleOkButton(view)

            val json = mPresenter!!.getSimpleJsonSampleResponse()
            Log.d("JSON_OUT", json.getString("userId") )
            Log.d("JSON_OUT", json.getString("id") )
            Log.d("JSON_OUT", json.getString("success") )

            mPresenter!!.getJsonSampleResponse()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        createFloatMenu(R.menu.menu_main, menu)
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_reset_counter -> {
                restCounter()
                updateMainContentText("$counter")
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun createFloatMenu(id: Int, menu: Menu) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun updateMainContentText(text: String) {
        val messageView: TextView = findViewById(R.id.main_content_text)
        messageView.text = text
    }

    override fun handleOkButton(view: View) {
        Snackbar.make(view, "Tapped $counter times.", Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
    }

    override fun restCounter() {
        counter = 0
    }

    override fun handleSuccess(result: Array<SinglePostResponse>) {
        Log.d(">>>> JSON >>>>", result.toString())
        Log.d(">>>> JSON >>>>", "SUCCESS")
    }




    override fun handleError(message: String) {
        Log.e(">>>> JSON >>>>", message)
        Log.e(">>>> JSON >>>>", "ERROR")
    }

}

