package com.example.standaloneapp1

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class InputFragment : Fragment(), View.OnClickListener {
    private var str_first: String? = null
    private var str_middle: String? = null
    private var str_last: String? = null

    private var et_first: EditText? = null
    private var et_middle: EditText? = null
    private var et_last: EditText? = null

    private var btn_submit: Button? = null
    private var btn_profile_picture: Button? = null

    private var iv_profile_picture: ImageView? = null
    private var bm_profile_picture: Bitmap? = null

    private var clickedProfile: Boolean? = null

    private var filePath: String? = null

    var data_sender : SendDataInterface? = null

    interface SendDataInterface {
        fun sendData(data: Array<String?>?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        data_sender = try {
            context as SendDataInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SubmitFragment.SendDataInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.input_fragment, container, false)

        et_first = view.findViewById(R.id.et_first_name) as EditText
        et_middle = view.findViewById(R.id.et_middle_name) as EditText
        et_last = view.findViewById(R.id.et_last_name) as EditText
        btn_submit = view.findViewById(R.id.btn_submit) as Button
        btn_profile_picture = view.findViewById(R.id.btn_profile_picture) as Button
        iv_profile_picture = view.findViewById(R.id.iv_profile_picture) as ImageView
        clickedProfile = false

        btn_submit!!.setOnClickListener(this)
        btn_profile_picture!!.setOnClickListener(this)

        if (savedInstanceState != null) {
            et_first!!.setText(savedInstanceState.getString("first_data"))
            et_middle!!.setText(savedInstanceState.getString("middle_data"))
            et_last!!.setText(savedInstanceState.getString("last_data"))
            filePath = savedInstanceState.getString("image_data")
            bm_profile_picture = BitmapFactory.decodeFile(filePath)
            iv_profile_picture!!.setImageBitmap(bm_profile_picture)
            clickedProfile = savedInstanceState.getBoolean("check_data")
        }

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_submit -> {
                str_first = et_first!!.text.toString()
                str_middle = et_middle!!.text.toString()
                str_last = et_last!!.text.toString()

                if (str_first.isNullOrBlank() && str_last.isNullOrBlank()) {
                    Toast.makeText(activity, "Must Enter First and Last name", Toast.LENGTH_SHORT)
                        .show()
                }
                else if (str_first.isNullOrBlank()) {
                    Toast.makeText(activity, "Must Enter First name", Toast.LENGTH_SHORT)
                        .show()
                }
                else if (str_last.isNullOrBlank()) {
                    Toast.makeText(activity, "Must Enter Last name", Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    if (clickedProfile == true) {
                        val nameList = arrayOf<String?>(str_first, str_last)
                        data_sender!!.sendData(nameList)
                    }
                    else {
                        Toast.makeText(activity, "Must Submit a Profile Picture", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            R.id.btn_profile_picture -> {
                val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    imageCapture.launch(camera)
                    clickedProfile = true
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(activity, "Profile Picture cannot be taken. Try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val imageCapture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val extras = result.data!!.extras
            bm_profile_picture = extras!!["data"] as Bitmap?

            if (isExternalStorageWritable) {
                iv_profile_picture!!.setImageBitmap(bm_profile_picture)
                filePath = saveImage(bm_profile_picture)
            } else {
                Toast.makeText(activity, "External storage not writable.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(activity, "file saved!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("image_data", filePath)
        outState.putBoolean("check_data", clickedProfile!!)
        super.onSaveInstanceState(outState)
    }
}