package com.example.myapplication.guolin.activity

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMediaStoreBinding
import com.example.myapplication.guolin.testclass.Image
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import java.util.ArrayList

class MediaStoreActivity : AppCompatActivity() {

    private val imageList = ArrayList<Image>()

    private val checkedImages = HashMap<String, Image>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMediaStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val pickFiles = intent.getBooleanExtra("")
        requestPermission()
    }

    fun requestPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    testMediaStoreAPI()
                } else {
                    Toast.makeText(this, "您拒绝读写权限", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun testMediaStoreAPI() {
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ljm", "image uri is $uri")
                val fd = contentResolver.openFileDescriptor(uri,"r")
                if (fd != null) {
                    val bitmap = BitmapFactory.decodeFileDescriptor(fd.fileDescriptor)

                }
            }
            cursor.close()
        }
    }
}