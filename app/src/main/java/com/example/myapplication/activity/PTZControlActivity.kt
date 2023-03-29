package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.customview.RockerView

class PTZControlActivity : AppCompatActivity() {
    lateinit var rockerLeft: RockerView
    lateinit var rockerRight: RockerView
    lateinit var logLeft: TextView
    lateinit var logRight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ptz_view_test)
        rockerLeft = findViewById(R.id.rockerView_left)
        rockerRight = findViewById(R.id.rockerView_right)
        logLeft = findViewById(R.id.log_left)
        logRight = findViewById(R.id.log_right)

        rockerLeft.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE)
        rockerLeft.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_8,
            object : RockerView.OnShakeListener {
                override fun onStart() {
                    logLeft.setText(null)
                }

                override fun direction(direction: RockerView.Direction?) {
                    logLeft.setText("摇动方向 : " + getDirection(direction!!))
                }

                override fun onFinish() {
                    logLeft.setText(null)
                }
            })

        rockerRight.setOnAngleChangeListener(object : RockerView.OnAngleChangeListener {
            override fun onStart() {
                logRight.setText(null)
            }

            override fun angle(angle: Double) {
                logRight.setText("摇动角度: " + angle)
            }

            override fun onFinish() {
                logRight.setText(null)
            }

        })
    }

    private fun getDirection(direction: RockerView.Direction): String? {
        var message: String? = null
        when (direction) {
            RockerView.Direction.DIRECTION_LEFT -> message = "左"
            RockerView.Direction.DIRECTION_RIGHT -> message = "右"
            RockerView.Direction.DIRECTION_UP -> message = "上"
            RockerView.Direction.DIRECTION_DOWN -> message = "下"
            RockerView.Direction.DIRECTION_UP_LEFT -> message = "左上"
            RockerView.Direction.DIRECTION_UP_RIGHT -> message = "右上"
            RockerView.Direction.DIRECTION_DOWN_LEFT -> message = "左下"
            RockerView.Direction.DIRECTION_DOWN_RIGHT -> message = "右下"
            else -> {
            }
        }
        return message
    }
}