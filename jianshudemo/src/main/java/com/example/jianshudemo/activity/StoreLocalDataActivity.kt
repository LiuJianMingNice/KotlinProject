package com.example.jianshudemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jianshudemo.R

const val TEST_NAME = "store"
class StoreLocalDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_local_data)
    }
}