package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTestMainBinding
import com.example.myapplication.factory.MainViewModelFactory
import com.example.myapplication.repository.MainRepository
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.TestViewModel




class TestMainActivity : BaseActivity<ActivityTestMainBinding, MainViewModel>(provideMainViewModelFactory()) {
//    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        //弃用
//        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    override fun initObserve() {
        viewModel.mUser.observe(this) {
            Log.d("ljm", "mUser==>> $it ")
        }
    }

    override fun ActivityTestMainBinding.initBinding() {

    }
}

fun provideMainViewModelFactory(): MainViewModelFactory {
    return MainViewModelFactory(MainRepository())
}