package com.example.standaloneapp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DisplayFragment : Fragment() {
    private var tv_first_name: TextView? = null
    private var tv_last_name: TextView? = null

    private var str_first_name: String? = null
    private var str_last_name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.display_fragment, container, false)

        tv_first_name = view.findViewById<View>(R.id.tv_first_name) as TextView
        tv_last_name = view.findViewById<View>(R.id.tv_last_name) as TextView

        if (savedInstanceState != null) {
            tv_first_name!!.text = savedInstanceState.getString("first_data")
            tv_last_name!!.text = savedInstanceState.getString("last_data")
            str_first_name = savedInstanceState.getString("first_data")
            str_last_name = savedInstanceState.getString("last_data")
        }
        else {
            val argumentBundle = arguments
            str_first_name = argumentBundle!!.getString("first_name")
            str_last_name = argumentBundle.getString("last_name")

            tv_first_name!!.text = str_first_name
            tv_last_name!!.text = str_last_name
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("first_data", str_first_name)
        outState.putString("last_data", str_last_name)
        super.onSaveInstanceState(outState)
    }
}