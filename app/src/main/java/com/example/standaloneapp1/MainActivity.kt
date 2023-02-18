package com.example.standaloneapp1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.security.KeyStore.TrustedCertificateEntry

class MainActivity : AppCompatActivity(), InputFragment.SendDataInterface {
    private var first_name: String? = null
    private var last_name: String? = null

    private var inputFragment: InputFragment? = null
    private var displayFragment: DisplayFragment? = null
    private var checkInputFragment: Boolean? = null
    private var checkDisplayFragment: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            checkInputFragment = true
            checkDisplayFragment = false
        }

        inputFragment = if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag("input_fragment") as InputFragment?
        } else {
            InputFragment()
        }
        displayFragment = if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag("display_fragment") as DisplayFragment?
        } else {
            DisplayFragment()
        }

        if (checkInputFragment == true) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, inputFragment!!, "input_fragment")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        else if (checkDisplayFragment == true) {
            val transaction = supportFragmentManager.beginTransaction()

            val sendData = Bundle()
            sendData.putString("first_name", first_name)
            sendData.putString("last_name", last_name)
            displayFragment!!.arguments = sendData

            transaction.replace(R.id.fragment_holder, displayFragment!!, "display_fragment")
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun sendData(data: Array<String?>?) {
        first_name = data!![0]
        last_name = data[1]

        val transaction = supportFragmentManager.beginTransaction()

        displayFragment = DisplayFragment()
        val sendData = Bundle()
        sendData.putString("first_name", first_name)
        sendData.putString("last_name", last_name)
        displayFragment!!.arguments = sendData

        transaction.replace(R.id.fragment_holder, displayFragment!!, "display_fragment")
        transaction.addToBackStack(null)
        transaction.commit()

        checkDisplayFragment = true
        checkInputFragment = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("display_data", checkDisplayFragment!!)
        outState.putBoolean("input_data", checkInputFragment!!)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        checkInputFragment = savedInstanceState.getBoolean("input_data")
        checkDisplayFragment = savedInstanceState.getBoolean("display_data")

        super.onRestoreInstanceState(savedInstanceState)
    }
}