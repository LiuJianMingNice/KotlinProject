package com.example.myapplication.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * PTZControl(支持摇杆和按钮点击)
 * @author liujianming
 * @date 2022-01-18
 */
class PTZControl(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val TAG = "PTZControl"

    private val DEFAULT_SIZE = 400
    private val DEFAULT_ROCKER_RADIUS = DEFAULT_SIZE / 8

    private var mAreaBackgroundPaint: Paint? = null
    private var mRockerPaint: Paint? = null

    private var mRockerPosition: Point? = null
    private var mCenterPoint: Point? = null

    private var mAreaRadius = 0
    private var mRockerRadius = 0

    private var mCallBackMode = CallBackMode.CALL_BACK_MODE_MOVE
    private var mOnAngleChangeListener: OnAngleChangeListener? = null
    private var mOnShakeListener: OnShakeListener? = null

    private var mDirectionMode: DirectionMode? = null
    private var tempDirection = Direction.DIRECTION_CENTER

    // 角度
    private val ANGLE_0 = 0.0
    private val ANGLE_360 = 360.0

    // 360°水平方向平分2份的边缘角度
    private val ANGLE_HORIZONTAL_2D_OF_0P = 90.0
    private val ANGLE_HORIZONTAL_2D_OF_1P = 270.0

    // 360°垂直方向平分2份的边缘角度
    private val ANGLE_VERTICAL_2D_OF_0P = 0.0
    private val ANGLE_VERTICAL_2D_OF_1P = 180.0

    // 360°平分4份的边缘角度
    private val ANGLE_4D_OF_0P = 0.0
    private val ANGLE_4D_OF_1P = 90.0
    private val ANGLE_4D_OF_2P = 180.0
    private val ANGLE_4D_OF_3P = 270.0

    // 360°平分4份的边缘角度(旋转45度)
    private val ANGLE_ROTATE45_4D_OF_0P = 45.0
    private val ANGLE_ROTATE45_4D_OF_1P = 135.0
    private val ANGLE_ROTATE45_4D_OF_2P = 225.0
    private val ANGLE_ROTATE45_4D_OF_3P = 315.0

    // 360°平分8份的边缘角度
    private val ANGLE_8D_OF_0P = 22.5
    private val ANGLE_8D_OF_1P = 67.5
    private val ANGLE_8D_OF_2P = 112.5
    private val ANGLE_8D_OF_3P = 157.5
    private val ANGLE_8D_OF_4P = 202.5
    private val ANGLE_8D_OF_5P = 247.5
    private val ANGLE_8D_OF_6P = 292.5
    private val ANGLE_8D_OF_7P = 337.5

    // 摇杆可移动区域背景
    private val AREA_BACKGROUND_MODE_PIC = 0
    private val AREA_BACKGROUND_MODE_COLOR = 1
    private val AREA_BACKGROUND_MODE_XML = 2
    private val AREA_BACKGROUND_MODE_DEFAULT = 3
    private var mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT
    private var mAreaBitmap: Bitmap? = null
    private var mTopBitmap: Bitmap? = null
    private var mBottomBitmap: Bitmap? = null
    private var mLeftBitmap: Bitmap? = null
    private var mRightBitmap: Bitmap? = null
    private var mAreaColor = 0

    // 摇杆背景
    private val ROCKER_BACKGROUND_MODE_PIC = 4
    private val ROCKER_BACKGROUND_MODE_COLOR = 5
    private val ROCKER_BACKGROUND_MODE_XML = 6
    private val ROCKER_BACKGROUND_MODE_DEFAULT = 7
    private var mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT
    private var mRockerBitmap: Bitmap? = null
    private var mRockerColor = 0

    private var topDrawable: StateListDrawable? = null
    private var bottomDrawable: StateListDrawable? = null
    private var leftDrawable: StateListDrawable? = null
    private var rightDrawable: StateListDrawable? = null

    private var isRocker: Boolean = false

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.RockerView)

        // 可移动区域背景
        val areaBackground = typedArray?.getDrawable(R.styleable.RockerView_areaBackground)

        topDrawable = typedArray?.getDrawable(R.styleable.RockerView_topControl) as StateListDrawable?
        bottomDrawable = typedArray?.getDrawable(R.styleable.RockerView_bottomControl) as StateListDrawable?
        leftDrawable = typedArray?.getDrawable(R.styleable.RockerView_leftControl) as StateListDrawable?
        rightDrawable = typedArray?.getDrawable(R.styleable.RockerView_rightControl) as StateListDrawable?
        isRocker = typedArray?.getBoolean(R.styleable.RockerView_isRocker, false)!!

        drawDirectionKeyBitmap(1, 1, 1, 1)

        if (null != areaBackground) {
            // 设置了背景
            if (areaBackground is BitmapDrawable) {
                // 设置了一张图片
                mAreaBitmap = areaBackground.bitmap
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_PIC
            } else if (areaBackground is GradientDrawable) {
                // XML
                mAreaBitmap = drawable2Bitmap(areaBackground)
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_XML
            } else if (areaBackground is ColorDrawable) {
                // 色值
                mAreaColor = areaBackground.color
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_COLOR
            } else {
                // 其他形式
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT
            }
        } else {
            // 没有设置背景
            mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT
        }
        // 摇杆背景
        val rockerBackground = typedArray?.getDrawable(R.styleable.RockerView_rockerBackground)
        if (null != rockerBackground) {
            // 设置了摇杆背景
            if (rockerBackground is BitmapDrawable) {
                // 图片
                mRockerBitmap = rockerBackground.bitmap
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_PIC
            } else if (rockerBackground is GradientDrawable) {
                // XML
                mRockerBitmap = drawable2Bitmap(rockerBackground)
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_XML
            } else if (rockerBackground is ColorDrawable) {
                // 色值
                mRockerColor = rockerBackground.color
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_COLOR
            } else {
                // 其他形式
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT
            }
        } else {
            // 没有设置摇杆背景
            mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT
        }

        // 摇杆半径
        mRockerRadius = typedArray?.getDimensionPixelOffset(
            R.styleable.RockerView_rockerRadius,
            DEFAULT_ROCKER_RADIUS
        ) ?:
                Log.i(
                    TAG,
                    "initAttribute: mAreaBackground = $areaBackground   mRockerBackground = $rockerBackground  mRockerRadius = $mRockerRadius"
                )
        typedArray?.recycle()


        // 移动区域画笔
        mAreaBackgroundPaint = Paint()
        mAreaBackgroundPaint!!.isAntiAlias = true

        // 摇杆画笔
        mRockerPaint = Paint()
        mRockerPaint!!.isAntiAlias = true

        // 中心点
        mCenterPoint = Point()
        // 摇杆位置
        mRockerPosition = Point()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth: Int
        val measureHeight: Int
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        measureWidth = if (widthMode == MeasureSpec.EXACTLY) {
            // 具体的值和match_parent
            widthSize
        } else {
            // wrap_content
            DEFAULT_SIZE
        }
        measureHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            DEFAULT_SIZE
        }
        Log.i(TAG, "onMeasure: --------------------------------------")
        Log.i(
            TAG,
            "onMeasure: widthMeasureSpec = $widthMeasureSpec heightMeasureSpec = $heightMeasureSpec"
        )
        Log.i(TAG, "onMeasure: widthMode = $widthMode  measureWidth = $widthSize")
        Log.i(TAG, "onMeasure: heightMode = $heightMode  measureHeight = $widthSize")
        Log.i(
            TAG,
            "onMeasure: measureWidth = $measureWidth measureHeight = $measureHeight"
        )
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        val cx = measuredWidth / 2
        val cy = measuredHeight / 2
        // 中心点
        mCenterPoint!![cx] = cy
        // 可移动区域的半径
        mAreaRadius = if (measuredWidth <= measuredHeight) cx else cy

        // 摇杆位置
        if (0 == mRockerPosition!!.x || 0 == mRockerPosition!!.y) {
            mRockerPosition!![mCenterPoint!!.x] = mCenterPoint!!.y
        }

        // 画可移动区域
        if (AREA_BACKGROUND_MODE_PIC == mAreaBackgroundMode || AREA_BACKGROUND_MODE_XML == mAreaBackgroundMode) {
            // 图片
            val src = Rect(0, 0, mAreaBitmap!!.width, mAreaBitmap!!.height)
            val dst = Rect(
                mCenterPoint!!.x - mAreaRadius,
                mCenterPoint!!.y - mAreaRadius,
                mCenterPoint!!.x + mAreaRadius,
                mCenterPoint!!.y + mAreaRadius
            )
            canvas.drawBitmap(mAreaBitmap!!, src, dst, mAreaBackgroundPaint)
        } else if (AREA_BACKGROUND_MODE_COLOR == mAreaBackgroundMode) {
            // 色值
            mAreaBackgroundPaint!!.color = mAreaColor
            canvas.drawCircle(
                mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat(), mAreaRadius.toFloat(),
                mAreaBackgroundPaint!!
            )
        } else {
            // 其他或者未设置
            mAreaBackgroundPaint!!.color = Color.GRAY
            canvas.drawCircle(
                mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat(), mAreaRadius.toFloat(),
                mAreaBackgroundPaint!!
            )
        }

        // 画摇杆
        if (isRocker) {
            if (ROCKER_BACKGROUND_MODE_PIC == mRockerBackgroundMode || ROCKER_BACKGROUND_MODE_XML == mRockerBackgroundMode) {
                // 图片
                val src = Rect(0, 0, mRockerBitmap!!.width, mRockerBitmap!!.height)
                val dst = Rect(
                    mRockerPosition!!.x - mRockerRadius,
                    mRockerPosition!!.y - mRockerRadius,
                    mRockerPosition!!.x + mRockerRadius,
                    mRockerPosition!!.y + mRockerRadius
                )
                canvas.drawBitmap(mRockerBitmap!!, src, dst, mRockerPaint)
            } else if (ROCKER_BACKGROUND_MODE_COLOR == mRockerBackgroundMode) {
                // 色值
                mRockerPaint!!.color = mRockerColor
                canvas.drawCircle(
                    mRockerPosition!!.x.toFloat(),
                    mRockerPosition!!.y.toFloat(),
                    mRockerRadius.toFloat(),
                    mRockerPaint!!
                )
            } else {
                // 其他或者未设置
                mRockerPaint!!.color = Color.RED
                canvas.drawCircle(
                    mRockerPosition!!.x.toFloat(),
                    mRockerPosition!!.y.toFloat(),
                    mRockerRadius.toFloat(),
                    mRockerPaint!!
                )
            }
        } else {
            mTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mTopBitmap!!.width / 2).toFloat(), 8f,  mRockerPaint) }
            mBottomBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mBottomBitmap!!.width / 2).toFloat(), (mCenterPoint!!.y - mBottomBitmap!!.height / 2).toFloat() * 2 - 8f,  mRockerPaint) }
            mLeftBitmap?.let { canvas.drawBitmap(it, 0f, (mCenterPoint!!.y - mLeftBitmap!!.height / 2).toFloat(),  mRockerPaint) }
            mRightBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mRightBitmap!!.width / 2).toFloat() * 2, (mCenterPoint!!.y - mRightBitmap!!.height / 2).toFloat(),  mRockerPaint) }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 回调 开始
                callBackStart()
                val moveX = event.x
                val moveY = event.y
                mRockerPosition = getRockerPositionPoint(
                    mCenterPoint, Point(
                        moveX.toInt(),
                        moveY.toInt()
                    ), mAreaRadius.toFloat(), mRockerRadius.toFloat()
                )
                moveRocker(mRockerPosition!!.x.toFloat(), mRockerPosition!!.y.toFloat())
//                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val moveY = event.y
                mRockerPosition = getRockerPositionPoint(
                    mCenterPoint, Point(
                        moveX.toInt(),
                        moveY.toInt()
                    ), mAreaRadius.toFloat(), mRockerRadius.toFloat()
                )
                moveRocker(mRockerPosition!!.x.toFloat(), mRockerPosition!!.y.toFloat())
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 回调 结束
                callBackFinish()
                val upX = event.x
                val upY = event.y
                moveRocker(mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat())
                Log.i(TAG, "onTouchEvent: 抬起位置 : x = $upX y = $upY")
                drawDirectionKeyBitmap(1, 1, 1, 1)
//                invalidate()
            }
        }
        return true
    }

    /**
     * 获取摇杆实际要显示的位置（点）
     *
     * @param centerPoint  中心点
     * @param touchPoint   触摸点
     * @param regionRadius 摇杆可活动区域半径
     * @param rockerRadius 摇杆半径
     * @return 摇杆实际显示的位置（点）
     */
    private fun getRockerPositionPoint(
        centerPoint: Point?,
        touchPoint: Point,
        regionRadius: Float,
        rockerRadius: Float
    ): Point? {
        // 两点在X轴的距离
        val lenX = (touchPoint.x - centerPoint!!.x).toFloat()
        // 两点在Y轴距离
        val lenY = (touchPoint.y - centerPoint.y).toFloat()
        // 两点距离
        val lenXY = Math.sqrt((lenX * lenX + lenY * lenY).toDouble()).toFloat()
        // 计算弧度
        val radian =
            Math.acos((lenX / lenXY).toDouble()) * if (touchPoint.y < centerPoint.y) -1 else 1
        // 计算角度
        val angle = radian2Angle(radian)

        // 回调 返回参数
        callBack(angle)
        Log.i(TAG, "getRockerPositionPoint: 角度 :$angle")
        return if (lenXY + rockerRadius <= regionRadius) { // 触摸位置在可活动范围内
            touchPoint
        } else { // 触摸位置在可活动范围以外
            // 计算要显示的位置
            val showPointX =
                (centerPoint.x + (regionRadius - rockerRadius) * Math.cos(radian)).toInt()
            val showPointY =
                (centerPoint.y + (regionRadius - rockerRadius) * Math.sin(radian)).toInt()
            Point(showPointX, showPointY)
        }
    }

    /**
     * 移动摇杆到指定位置
     *
     * @param x x坐标
     * @param y y坐标
     */
    private fun moveRocker(x: Float, y: Float) {
        mRockerPosition!![x.toInt()] = y.toInt()
        Log.i(
            TAG,
            "onTouchEvent: 移动位置 : x = " + mRockerPosition!!.x + " y = " + mRockerPosition!!.y
        )
        invalidate()
    }

    /**
     * 弧度转角度
     *
     * @param radian 弧度
     * @return 角度[0, 360)
     */
    private fun radian2Angle(radian: Double): Double {
        val tmp = Math.round(radian / Math.PI * 180).toDouble()
        return if (tmp >= 0) tmp else 360 + tmp
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    private fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        // 取 drawable 的长宽
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        // 取 drawable 的颜色格式
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(width, height, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun drawDirectionKeyBitmap(top: Int, bottom: Int, left: Int, right: Int) {
        topDrawable?.selectDrawable(top)
        mTopBitmap = topDrawable?.toBitmap()
        bottomDrawable?.selectDrawable(bottom)
        mBottomBitmap = bottomDrawable?.toBitmap()
        leftDrawable?.selectDrawable(left)
        mLeftBitmap = leftDrawable?.toBitmap()
        rightDrawable?.selectDrawable(right)
        mRightBitmap = rightDrawable?.toBitmap()
    }

    /**
     * 回调
     * 开始
     */
    private fun callBackStart() {
        tempDirection = Direction.DIRECTION_CENTER
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener!!.onStart()
        }
        if (null != mOnShakeListener) {
            mOnShakeListener!!.onStart()
        }
    }

    /**
     * 回调
     * 返回参数
     *
     * @param angle 摇动角度
     */
    private fun callBack(angle: Double) {
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener!!.angle(angle)
        }

        if (null != mOnShakeListener) {
            if (CallBackMode.CALL_BACK_MODE_MOVE == mCallBackMode) {
                when (mDirectionMode) {
                    DirectionMode.DIRECTION_2_HORIZONTAL -> if (ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) {
                        // 右
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                    } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle) {
                        // 左
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                    }
                    DirectionMode.DIRECTION_2_VERTICAL -> if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle) {
                        // 下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                    } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle) {
                        // 上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                    }
                    DirectionMode.DIRECTION_4_ROTATE_0 -> if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle) {
                        // 右下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                    } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle) {
                        // 左下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                    } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle) {
                        // 左上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                    } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle) {
                        // 右上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                    }
                    DirectionMode.DIRECTION_4_ROTATE_45 -> if (ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) {
                        // 右
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                    } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle) {
                        // 下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                    } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle) {
                        // 左
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                    } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle) {
                        // 上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                    }
                    DirectionMode.DIRECTION_8 -> if (ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) {
                        // 右
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                    } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle) {
                        // 右下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                    } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle) {
                        // 下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                    } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle) {
                        // 左下
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                    } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle) {
                        // 左
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                    } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle) {
                        // 左上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                    } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle) {
                        // 上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                    } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle) {
                        // 右上
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                    }
                    else -> {
                    }
                }
            } else if (CallBackMode.CALL_BACK_MODE_STATE_CHANGE == mCallBackMode) {

                val isEight = false

                if (isEight) {
                    when (mDirectionMode) {
                        DirectionMode.DIRECTION_2_HORIZONTAL -> if ((ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        }
                        DirectionMode.DIRECTION_2_VERTICAL -> if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        }
                        DirectionMode.DIRECTION_4_ROTATE_0 -> if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        }
                        DirectionMode.DIRECTION_4_ROTATE_45 -> if ((ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        }
                        DirectionMode.DIRECTION_8 -> if ((ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        }
                        else -> {
                        }
                    }
                } else {
                    Log.d("ljm", "mCallBackMode===>> " + mCallBackMode)
                    Log.d("ljm", "mDirectionMode==>>>" + mDirectionMode)
                    Log.d("ljm", "angle===>> " + angle)
                    when (mDirectionMode) {
                        DirectionMode.DIRECTION_2_HORIZONTAL -> if ((ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            drawDirectionKeyBitmap(1, 1, 1, 0)
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            drawDirectionKeyBitmap(1, 1, 0, 1)
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        }
                        DirectionMode.DIRECTION_2_VERTICAL -> if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            drawDirectionKeyBitmap(1, 0, 1, 1)
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            drawDirectionKeyBitmap(0, 1, 1, 1)
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        }
                        DirectionMode.DIRECTION_4_ROTATE_0 -> if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        }
                        DirectionMode.DIRECTION_4_ROTATE_45 -> if ((ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            drawDirectionKeyBitmap(1, 1, 1, 0)
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            drawDirectionKeyBitmap(1, 0, 1, 1)
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            drawDirectionKeyBitmap(1, 1, 0, 1)
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            drawDirectionKeyBitmap(0, 1, 1, 1)
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        }
                        DirectionMode.DIRECTION_8 -> if ((ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            drawDirectionKeyBitmap(1, 1, 1, 0)
                            tempDirection = Direction.DIRECTION_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            drawDirectionKeyBitmap(1, 0, 1, 1)
                            tempDirection = Direction.DIRECTION_DOWN
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            drawDirectionKeyBitmap(1, 1, 0, 1)
                            tempDirection = Direction.DIRECTION_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            drawDirectionKeyBitmap(0, 1, 1, 1)
                            tempDirection = Direction.DIRECTION_UP
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT
                            mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        }
                        else -> {
                        }
                    }
                }



            }
        }
    }

    /**
     * 回调
     * 结束
     */
    private fun callBackFinish() {
        tempDirection = Direction.DIRECTION_CENTER
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener!!.onFinish()
        }
        if (null != mOnShakeListener) {
            mOnShakeListener!!.onFinish()
        }
    }

    /**
     * 回调模式
     */
    enum class CallBackMode {
        CALL_BACK_MODE_MOVE,  // 有移动就立刻回调
        CALL_BACK_MODE_STATE_CHANGE  // 只有状态变化的时候才回调
    }

    /**
     * 设置回调模式
     *
     * @param mode 回调模式
     */
    fun setCallBackMode(mode: CallBackMode) {
        mCallBackMode = mode
    }

    /**
     * 摇杆支持几个方向
     */
    enum class DirectionMode {
        DIRECTION_2_HORIZONTAL,  // 横向 左右两个方向
        DIRECTION_2_VERTICAL,  // 纵向 上下两个方向
        DIRECTION_4_ROTATE_0,  // 四个方向
        DIRECTION_4_ROTATE_45,  // 四个方向 旋转45度
        DIRECTION_8 // 八个方向
    }

    /**
     * 方向
     */
    enum class Direction {
        DIRECTION_LEFT,  // 左
        DIRECTION_RIGHT,  // 右
        DIRECTION_UP,  // 上
        DIRECTION_DOWN,  // 下
        DIRECTION_UP_LEFT,  // 左上
        DIRECTION_UP_RIGHT,  // 右上
        DIRECTION_DOWN_LEFT,  // 左下
        DIRECTION_DOWN_RIGHT,  // 右下
        DIRECTION_CENTER // 中间
    }

    /**
     * 添加摇杆摇动角度的监听
     *
     * @param listener 回调接口
     */
    fun setOnAngleChangeListener(listener: OnAngleChangeListener?) {
        mOnAngleChangeListener = listener
    }

    /**
     * 添加摇动的监听
     *
     * @param directionMode 监听的方向
     * @param listener      回调
     */
    fun setOnShakeListener(directionMode: DirectionMode?, listener: OnShakeListener?) {
        mDirectionMode = directionMode
        mOnShakeListener = listener
    }

    /**
     * 摇动方向监听接口
     */
    interface OnShakeListener {
        // 开始
        fun onStart()

        /**
         * 摇动方向
         *
         * @param direction 方向
         */
        fun direction(direction: Direction?)

        // 结束
        fun onFinish()
    }

    /**
     * 摇动角度的监听接口
     */
    interface OnAngleChangeListener {
        // 开始
        fun onStart()

        /**
         * 摇杆角度变化
         *
         * @param angle 角度[0,360)
         */
        fun angle(angle: Double)

        // 结束
        fun onFinish()
    }
}