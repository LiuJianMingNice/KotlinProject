package com.example.myapplication.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.permissionx.guolindev.PermissionX

class PermissionTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_test)
//        requestPermissions()
        requestPermissionsForPermissionX()
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            1 -> {
//                val denied = ArrayList<String>()
//                val deniedAndNeverAskAgain = ArrayList<String>()
//                Log.d("ljm", "denied.size==1==>> " + denied.size)
//                Log.d("ljm", "deniedAndNeverAskAgain.size==1==>> " + deniedAndNeverAskAgain.size)
//                grantResults.forEachIndexed { index, result ->
//                    Log.d("ljm", "result==>> " + result)
//                    if (result != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[index])) {
//                            denied.add(permissions[index])
//                        } else {
//                            deniedAndNeverAskAgain.add(permissions[index])
//                        }
//                    }
//                }
//                Log.d("ljm", "denied.size==2==>> " + denied.size)
//                Log.d("ljm", "deniedAndNeverAskAgain.size==2==>> " + deniedAndNeverAskAgain.size)
//                if (denied.isEmpty() && deniedAndNeverAskAgain.isEmpty()) {
//                    takePicture()
//                } else {
//                    if (denied.isNotEmpty()) {
//                        AlertDialog.Builder(this).apply {
//                            setMessage("拍照功能需要您同意相册和定位权限")
//                            setCancelable(false)
//                            setPositiveButton("确定") {_, _ ->
//                                requestPermissions()
//                            }
//                        }.show()
//                    } else {
//                        AlertDialog.Builder(this).apply {
//                            setMessage("您需要去设置当中同意相册和定位权限")
//                            setCancelable(false)
//                            setPositiveButton("确定") { _, _ ->
//                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                val uri = Uri.fromParts("package", packageName, null)
//                                intent.data = uri
//                                startActivityForResult(intent, 1)
//                            }
//                        }.show()
//                    }
//                }
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            1 -> {
//                requestPermissions()
//            }
//        }
//    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
    }

    fun requestPermissionsForPermissionX() {
        PermissionX.init(this)
            .permissions(Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                val message = "拍照功能需要您同意相册和定位权限"
                val ok = "确定"
                scope.showRequestReasonDialog(deniedList, message, ok)
            }
            .onForwardToSettings { scope, deniedList ->
                val message = "您需要去设置当中同意相册和定位权限"
                val ok = "确定"
                scope.showForwardToSettingsDialog(deniedList, message, ok)
            }
            .request { _, _, _ ->
                takePicture()
            }
    }

    fun takePicture() {
        Toast.makeText(this, "开始拍照", Toast.LENGTH_SHORT).show()
    }
}