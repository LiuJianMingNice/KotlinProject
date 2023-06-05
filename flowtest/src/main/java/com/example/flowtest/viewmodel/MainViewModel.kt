package com.example.flowtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * MainViewModel
 * @author liujianming
 * @date 2023-05-05
 */
class MainViewModel : ViewModel() {

    val timeFlow = flow {
        var time = 0
        while (true) {
            emit(time)
            delay(1000)
            time++
        }
    }

    val stateFlow1 = timeFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _stateFlow = MutableStateFlow(0)

    val stateFlow = _stateFlow.asStateFlow()

    fun startTimer() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                _stateFlow.value += 1
            }
        }, 0, 1000)
    }

    private val _clickCountFlow = MutableStateFlow(0)

    val clickCountFlow = _clickCountFlow.asStateFlow()

    fun increaseClickCount() {
        _clickCountFlow.value += 1
    }

    private val _loginFlow = MutableStateFlow("")
    val loginFlow = _loginFlow.asStateFlow()

    fun startLogin() {
        _loginFlow.value = "Login Success"
    }

    private val _loginSharedFlow = MutableSharedFlow<String>()
    val loginSharedFlow = _loginSharedFlow.asSharedFlow()

    fun startLoginForSharedFlow() {
        viewModelScope.launch {
            _loginSharedFlow.emit("Login Success")
        }
    }
}