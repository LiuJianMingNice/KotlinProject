package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*

const val TAG = "MainActivity"
const val MAX_COUNT = 8

class MainActivity : AppCompatActivity() {


    private lateinit var btn: Button
//    val TAG: String = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn_click)
        btn.setOnClickListener {
//            start()
//            start1()
//            start2()
//            start3()
//            testUnDispatched()
//            testCoroutineExceptionHandler()
//            testException()
//            testException1()
            testException2()
        }
    }

    private fun start() {
        val runBlockingJob = runBlocking {
            Log.d(TAG, "runBlocking=启动一个协程")
        }
        Log.d(TAG, "runBlocking=$runBlockingJob")
        val launchJob = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "launch=启动一个协程")
        }
        Log.d(TAG, "launch=$launchJob")
        val asyncJob = GlobalScope.async {
            Log.d(TAG, "async=启动一个协程")
        }
        Log.d(TAG, "async=$asyncJob")
    }

    private fun start1(){
        GlobalScope.launch{
            val launchJob = launch{
                Log.d(TAG, "启动一个协程")
            }
            Log.d(TAG, "launchJob:$launchJob")
            val asyncJob = async{
                Log.d(TAG, "启动一个协程")
                "我是async返回值"
            }
            Log.d(TAG, "asyncJob.await():${asyncJob.await()}")
            Log.d(TAG, "asyncJob:$asyncJob")
        }
    }

    private fun start2() {
        GlobalScope.launch(Dispatchers.Main) {
            for (index in 1 until  10) {
                //同步执行
                launch {
                    Log.d("launch$index", "启动一个协程")
                }
            }
        }
    }

    private fun start3() {
        GlobalScope.launch {
            for (index in 1 until  10) {
                //并发执行
                launch {
                    Log.d("launch$index", "启动一个协程")
                }
            }
        }
    }

    private fun start4() {
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                "请求结果"
            }

            btn.text = result
        }
    }

    private fun testUnDispatched() {
        GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                Log.d(TAG, "testUnDispatched: ${Thread.currentThread().name}线程 -> 挂起前")
                delay(100)
                Log.d(TAG, "testUnDispatched: ${Thread.currentThread().name}线程 -> 挂起后")
            }
            Log.d(TAG, "testUnDispatched: ${Thread.currentThread().name}线程 -> join前")
            job.join()
            Log.d(TAG, "testUnDispatched: ${Thread.currentThread().name}线程 -> join后")
        }
    }

    private fun testCoroutineExceptionHandler() {
        GlobalScope.launch {
            val job = launch {
                Log.d(TAG, "testCoroutineExceptionHandler: ${Thread.currentThread().name} --> 抛出未捕获异常")
                throw NullPointerException("异常测试")
            }
            job.join()
            Log.d(TAG, "testCoroutineExceptionHandler: ${Thread.currentThread().name} --> end")
        }
    }

    private fun testException() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "testException: ${coroutineContext[CoroutineName]} : $throwable")
        }
        GlobalScope.launch(CoroutineName("异常处理") + exceptionHandler) {
            val job = launch {
                Log.d(TAG, "testException: ${Thread.currentThread().name} --> 我要开始抛异常了")
                throw NullPointerException("异常测试")
            }
            Log.d(TAG, "testException: ${Thread.currentThread().name} --> end")
        }
    }
    
    private fun testException1() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "testException1: ${coroutineContext[CoroutineName]} 处理异常 : $throwable")
        }
        GlobalScope.launch(CoroutineName("父协程") + exceptionHandler) {
            val job = launch(CoroutineName("子协程")) {
                Log.d(TAG, "testException1: ${Thread.currentThread().name} ---> 我要开始抛异常了")
                for (index in 0..10) {
                    launch(CoroutineName("孙子协程$index")) {
                        Log.d(TAG, "testException1: ${Thread.currentThread().name} ---> ${coroutineContext[CoroutineName]}")
                    }
                }
                throw NullPointerException("空指针异常")
            }
            for (index in 0..10) {
                launch(CoroutineName("子协程$index")) {
                    Log.d(TAG, "testException1: ${Thread.currentThread().name} ---> ${coroutineContext[CoroutineName]}")
                }
            }
            try {
                job.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.d(TAG, "testException1: ${Thread.currentThread().name} ---> end")
        }
    }
    
    private fun testException2() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "testException2: ${coroutineContext[CoroutineName].toString()} 处理异常： $throwable")
        }
        GlobalScope.launch(exceptionHandler) {
            supervisorScope {
                launch(CoroutineName("异常子协程")) {
                    Log.d(TAG, "testException2: ${Thread.currentThread().name} ---> 我要开始抛异常了")
                    throw NullPointerException("空指针异常")
                }
                for (index in 0..10) {
                    launch(CoroutineName("子协程$index")) {
                        Log.d(TAG, "testException2: ${Thread.currentThread().name} 正常执行 ---> $index")
                        if (index %3 == 0) {
                            throw java.lang.NullPointerException("子协程${index}空指针异常")
                        }
                    }
                }
            }
        }
    }

    private fun testException3() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "testException3: ${coroutineContext[CoroutineName].toString()} 处理异常：$throwable ")
        }

        val supervisorScope = CoroutineScope(SupervisorJob() + exceptionHandler)
    }

    var job:Job? = null
    private fun start5() {
        job = GlobalScope.launch(Dispatchers.Main + SupervisorJob()) {
            launch {
                throw NullPointerException("空指针")
            }
            val result = withContext(Dispatchers.IO) {
                //网络请求
                "请求结果"
            }
            launch {
                //网络请求3
            }
            btn.text = result
        }
    }

    public val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope
        get() = lifecycle.coroutineScope

    public val Lifecycle.coroutineScope: LifecycleCoroutineScope
        get() {
            while(true) {
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}