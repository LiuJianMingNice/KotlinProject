package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.customview.PTZControl
import com.example.myapplication.customview.PTZControl.OnShakeListener

class RockerViewActivity : AppCompatActivity() {

    lateinit var rockerLeft: PTZControl
//    lateinit var rockerRight: PTZControl
    lateinit var logLeft: TextView
    lateinit var logRight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocker_view)
        rockerLeft = findViewById(R.id.rockerView_left)
//        rockerRight = findViewById(R.id.rockerView_right)
        logLeft = findViewById(R.id.log_left)
        logRight = findViewById(R.id.log_right)
//
        rockerLeft.setCallBackMode(PTZControl.CallBackMode.CALL_BACK_MODE_STATE_CHANGE)
        rockerLeft.setOnShakeListener(
            PTZControl.DirectionMode.DIRECTION_4_ROTATE_45,
            object : OnShakeListener {
                override fun onStart() {
                    logLeft.setText(null)
                }

                override fun direction(direction: PTZControl.Direction?) {
                    logLeft.setText("摇动方向 : " + getDirection(direction!!))
                }


                override fun onFinish() {
                    logLeft.setText(null)
                }
            })

//        rockerRight.setOnAngleChangeListener(object : PTZControl.OnAngleChangeListener {
//            override fun onStart() {
//                logRight.setText(null)
//            }
//
//            override fun angle(angle: Double) {
//                logRight.setText("摇动角度: " + angle)
//            }
//
//            override fun onFinish() {
//                logRight.setText(null)
//            }
//
//        })
    }

    private fun getDirection(direction: PTZControl.Direction): String? {
        var message: String? = null
        when (direction) {
            PTZControl.Direction.DIRECTION_LEFT -> message = "左"
            PTZControl.Direction.DIRECTION_RIGHT -> message = "右"
            PTZControl.Direction.DIRECTION_UP -> message = "上"
            PTZControl.Direction.DIRECTION_DOWN -> message = "下"
            PTZControl.Direction.DIRECTION_UP_LEFT -> message = "左上"
            PTZControl.Direction.DIRECTION_UP_RIGHT -> message = "右上"
            PTZControl.Direction.DIRECTION_DOWN_LEFT -> message = "左下"
            PTZControl.Direction.DIRECTION_DOWN_RIGHT -> message = "右下"
            else -> {
            }
        }
        return message
    }
}