package com.example.khj_pc.twoday

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val PICKFILE_RESULT_CODE = 1
    lateinit var uri : Uri
    lateinit var file : File

    companion object {
        val TAG : String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText.customSelectionActionModeCallback = CustomCallback(this, editText)

        setListeners()
    }

    fun setListeners() {
        button.setOnClickListener {
            if(EasyPermissions.hasPermissions(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestFile()
            } else {
                EasyPermissions.requestPermissions(this, "tex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    fun requestFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "file/*"
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,
                "tex 파일을 선택해주세요!"),PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKFILE_RESULT_CODE) {
            if (EasyPermissions.hasPermissions(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                uri = data!!.data!! //이게 받은 uri
                Log.d(TAG, uri.toString())
            } else {
                EasyPermissions.requestPermissions(this@MainActivity, "ex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        EasyPermissions.requestPermissions(this, "tex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        requestFile()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



}

