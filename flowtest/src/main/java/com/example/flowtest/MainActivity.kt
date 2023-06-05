package com.example.flowtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flowtest.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
//            lifecycleScope.launchWhenStarted {
////                mainViewModel.timeFlow.collectLatest { time ->
////                    textView.text = time.toString()
//////                        delay(3000)
////                    Log.d("ljm", "Update time $time in UI.")
////                }
//
//                repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    mainViewModel.timeFlow.collect { time ->
//                        textView.text = time.toString()
//                        Log.d("ljm", "Update time $time in UI.")
//                    }
//                }
//            }

//            mainViewModel.startTimer()

//            mainViewModel.increaseClickCount()

//            mainViewModel.startLogin()

            mainViewModel.startLoginForSharedFlow()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mainViewModel.timeFlow.collect {
//                    textView.text = it.toString()
//                }

//                mainViewModel.stateFlow1.collect { time ->
//                    textView.text = time.toString()
//                }

//                mainViewModel.clickCountFlow.collect { time ->
//                    textView.text = time.toString()
//                }

//                mainViewModel.loginFlow.collect {
//                    if (it.isNotBlank()) {
//                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
//                    }
//                }

                mainViewModel.loginSharedFlow.collect {
                    if (it.isNotBlank()) {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}