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
 * DirectionControlView
 * @author liujianming
 * @date 2022-02-09
 */
class DirectionControlView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val _tag = "DirectionControlView"

    private val DEFAULT_SIZE = 400

    private var mAreaBackgroundPaint: Paint? = null
    private var mControlPaint: Paint? = null

    private var mCenterPoint: Point? = null

    private var mAreaRadius = 0

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

    // 方向区域背景
    private val AREA_BACKGROUND_MODE_PIC = 0
    private val AREA_BACKGROUND_MODE_COLOR = 1
    private val AREA_BACKGROUND_MODE_XML = 2
    private val AREA_BACKGROUND_MODE_DEFAULT = 3
    private var mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT
    private var mAreaBitmap: Bitmap? = null
    private var mAreaColor = 0

    //方向键
    private var mTopBitmap: Bitmap? = null
    private var mBottomBitmap: Bitmap? = null
    private var mLeftBitmap: Bitmap? = null
    private var mRightBitmap: Bitmap? = null
    private var mLeftTopBitmap: Bitmap? = null
    private var mRightTopBitmap: Bitmap? = null
    private var mLeftBottomBitmap: Bitmap? = null
    private var mRightBottomBitmap: Bitmap? = null

    private var mTopDrawable: StateListDrawable? = null
    private var mBottomDrawable: StateListDrawable? = null
    private var mLeftDrawable: StateListDrawable? = null
    private var mRightDrawable: StateListDrawable? = null
    private var mLeftTopDrawable: StateListDrawable? = null
    private var mRightTopDrawable: StateListDrawable? = null
    private var mLeftBottomDrawable: StateListDrawable? = null
    private var mRightBottomDrawable: StateListDrawable? = null

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.RockerView)

        // 区域背景
        val areaBackground = typedArray?.getDrawable(R.styleable.RockerView_areaBackground)

        mTopDrawable = typedArray?.getDrawable(R.styleable.RockerView_topControl) as StateListDrawable?
        mBottomDrawable = typedArray?.getDrawable(R.styleable.RockerView_bottomControl) as StateListDrawable?
        mLeftDrawable = typedArray?.getDrawable(R.styleable.RockerView_leftControl) as StateListDrawable?
        mRightDrawable = typedArray?.getDrawable(R.styleable.RockerView_rightControl) as StateListDrawable?
        mLeftTopDrawable = typedArray?.getDrawable(R.styleable.RockerView_leftTopControl) as StateListDrawable?
        mRightTopDrawable = typedArray?.getDrawable(R.styleable.RockerView_rightTopControl) as StateListDrawable?
        mLeftBottomDrawable = typedArray?.getDrawable(R.styleable.RockerView_leftBottomControl) as StateListDrawable?
        mRightBottomDrawable = typedArray?.getDrawable(R.styleable.RockerView_rightBottomControl) as StateListDrawable?

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

//        when (mDirectionMode) {
//            DirectionMode.DIRECTION_2_HORIZONTAL -> drawDirectionKey2Horizontal(1, 1)
//            DirectionMode.DIRECTION_2_VERTICAL -> drawDirectionKey2Vertical(1, 1)
//            DirectionMode.DIRECTION_4_ROTATE_0 -> drawDirectionKey4Rotate0Bitmap(1, 1, 1, 1)
//            DirectionMode.DIRECTION_4_ROTATE_45 -> drawDirectionKey4Rotate45Bitmap(1, 1, 1, 1)
//            DirectionMode.DIRECTION_8 -> drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 1, 1, 1,)
//        }

        drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 1, 1, 1,)

        // 移动区域画笔
        mAreaBackgroundPaint = Paint()
        mAreaBackgroundPaint!!.isAntiAlias = true

        // 控制方向画笔
        mControlPaint = Paint()
        mControlPaint!!.isAntiAlias = true

        // 中心点
        mCenterPoint = Point()
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
        Log.i(_tag, "onMeasure: --------------------------------------")
        Log.i(
            _tag,
            "onMeasure: widthMeasureSpec = $widthMeasureSpec heightMeasureSpec = $heightMeasureSpec"
        )
        Log.i(_tag, "onMeasure: widthMode = $widthMode  measureWidth = $widthSize")
        Log.i(_tag, "onMeasure: heightMode = $heightMode  measureHeight = $widthSize")
        Log.i(
            _tag,
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
        // 可点击区域的半径
        mAreaRadius = if (measuredWidth <= measuredHeight) cx else cy

        // 画可点击区域
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

        when(mDirectionMode) {
            DirectionMode.DIRECTION_2_HORIZONTAL -> drawDirection2Horizontal(canvas)
            DirectionMode.DIRECTION_2_VERTICAL -> drawDirection2Vertical(canvas)
            DirectionMode.DIRECTION_4_ROTATE_0 -> drawDirection4Rotate0(canvas)
            DirectionMode.DIRECTION_4_ROTATE_45 -> drawDirection4Rotate45(canvas)
            DirectionMode.DIRECTION_8 -> drawDirection4Rotate45(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 回调 开始
                callBackStart()
                val moveX = event.x
                val moveY = event.y
                getAngle(mCenterPoint, Point(moveX.toInt(), moveY.toInt()))
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val moveY = event.y
                getAngle(mCenterPoint,Point(
                        moveX.toInt(),
                        moveY.toInt()
                    ))
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 回调 结束
                callBackFinish()
//                drawDirectionKey4Rotate45Bitmap(1, 1, 1, 1)
                drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 1, 1, 1,)
                invalidate()
            }
        }
        return true
    }

    /**
     * 获取触摸点与中心点的角度
     *
     * @param centerPoint  中心点
     * @param touchPoint   触摸点
     */
    private fun getAngle(
        centerPoint: Point?,
        touchPoint: Point
    ) {
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
        invalidate()
        Log.i(_tag, "getRockerPositionPoint: 角度 :$angle")
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

    private fun drawDirection2Horizontal(canvas: Canvas) {
        mLeftBitmap?.let { canvas.drawBitmap(it, 0f, (mCenterPoint!!.y - mLeftBitmap!!.height / 2).toFloat(),  mControlPaint) }
        mRightBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mRightBitmap!!.width / 2).toFloat() * 2, (mCenterPoint!!.y - mRightBitmap!!.height / 2).toFloat(),  mControlPaint) }
    }

    private fun drawDirection2Vertical(canvas: Canvas) {
        mTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mTopBitmap!!.width / 2).toFloat(), 0f,  mControlPaint) }
        mBottomBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mBottomBitmap!!.width / 2).toFloat(), (mCenterPoint!!.y - mBottomBitmap!!.height / 2).toFloat() * 2,  mControlPaint) }
    }

    private fun drawDirection4Rotate0(canvas: Canvas) {
        Log.d("ljc", "x:${mCenterPoint!!.x},y:${mCenterPoint!!.y}")
        Log.d("ljc", "x:${mLeftTopBitmap!!.width},y:${mLeftBitmap!!.height}")
        Log.d("ljc", "x:${mTopBitmap!!.width},y:${mRightBitmap!!.width}")
//        mLeftTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mTopBitmap!!.width).toFloat(), 0f,  mControlPaint) }
        mLeftTopBitmap?.let { canvas.drawBitmap(it, ((mCenterPoint!!.x - (mLeftTopBitmap!!.width / 2)) * Math.sin(Math.toRadians(45.0))).toFloat(), ((mCenterPoint!!.y - (mLeftTopBitmap!!.height / 2)) * Math.sin(Math.toRadians(45.0))).toFloat(),  mControlPaint) }
        mRightTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mBottomBitmap!!.width / 2).toFloat(), (mCenterPoint!!.y - mBottomBitmap!!.height / 2).toFloat() * 2,  mControlPaint) }
        mLeftBottomBitmap?.let { canvas.drawBitmap(it, 0f, (mCenterPoint!!.y - mLeftBitmap!!.height / 2).toFloat(),  mControlPaint) }
        mRightBottomBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mRightBitmap!!.width / 2).toFloat() * 2, (mCenterPoint!!.y - mRightBitmap!!.height / 2).toFloat(),  mControlPaint) }
    }

    private fun drawDirection4Rotate45(canvas: Canvas) {
        mTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mTopBitmap!!.height / 2).toFloat(), 0f,  mControlPaint) }
        mBottomBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mBottomBitmap!!.height / 2).toFloat(), (mCenterPoint!!.y - mBottomBitmap!!.width / 2).toFloat() * 2,  mControlPaint) }
        mLeftBitmap?.let { canvas.drawBitmap(it, 0f, (mCenterPoint!!.y - mLeftBitmap!!.height / 2).toFloat(),  mControlPaint) }
        mRightBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mRightBitmap!!.height / 2).toFloat() * 2, (mCenterPoint!!.y - mRightBitmap!!.width / 2).toFloat(),  mControlPaint) }
    }

    private fun drawDirection8(canvas: Canvas) {
        mTopBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mTopBitmap!!.width / 2).toFloat(), 0f,  mControlPaint) }
        mBottomBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mBottomBitmap!!.width / 2).toFloat(), (mCenterPoint!!.y - mBottomBitmap!!.height / 2).toFloat() * 2,  mControlPaint) }
        mLeftBitmap?.let { canvas.drawBitmap(it, 0f, (mCenterPoint!!.y - mLeftBitmap!!.height / 2).toFloat(),  mControlPaint) }
        mRightBitmap?.let { canvas.drawBitmap(it, (mCenterPoint!!.x - mRightBitmap!!.width / 2).toFloat() * 2, (mCenterPoint!!.y - mRightBitmap!!.height / 2).toFloat(),  mControlPaint) }
    }

    private fun drawDirectionKey2Horizontal(left: Int, right: Int) {
        mLeftDrawable?.selectDrawable(left)
        mLeftBitmap = mLeftDrawable?.toBitmap()
        mRightDrawable?.selectDrawable(right)
        mRightBitmap = mRightDrawable?.toBitmap()
    }

    private fun drawDirectionKey2Vertical(top: Int, bottom: Int) {
        mTopDrawable?.selectDrawable(top)
        mTopBitmap = mTopDrawable?.toBitmap()
        mBottomDrawable?.selectDrawable(bottom)
        mBottomBitmap = mBottomDrawable?.toBitmap()
    }

    private fun drawDirectionKey4Rotate0Bitmap(leftTop: Int, rightTop: Int, leftBottom: Int, rightBottom: Int) {
        mLeftTopDrawable?.selectDrawable(leftTop)
        mLeftTopBitmap = mLeftTopDrawable?.toBitmap()
        mRightTopDrawable?.selectDrawable(rightTop)
        mRightTopBitmap = mRightTopDrawable?.toBitmap()
        mLeftBottomDrawable?.selectDrawable(leftBottom)
        mLeftBottomBitmap = mLeftBottomDrawable?.toBitmap()
        mRightBottomDrawable?.selectDrawable(rightBottom)
        mRightBottomBitmap = mRightBottomDrawable?.toBitmap()
    }

    private fun drawDirectionKey4Rotate45Bitmap(top: Int, bottom: Int, left: Int, right: Int) {
        mTopDrawable?.selectDrawable(top)
        mTopBitmap = mTopDrawable?.toBitmap()
        mBottomDrawable?.selectDrawable(bottom)
        mBottomBitmap = mBottomDrawable?.toBitmap()
        mLeftDrawable?.selectDrawable(left)
        mLeftBitmap = mLeftDrawable?.toBitmap()
        mRightDrawable?.selectDrawable(right)
        mRightBitmap = mRightDrawable?.toBitmap()
    }

    private fun drawDirectionKey8Bitmap(top: Int, bottom: Int, left: Int, right: Int, leftTop: Int, rightTop: Int, leftBottom: Int, rightBottom: Int) {
        mTopDrawable?.selectDrawable(top)
        mTopBitmap = mTopDrawable?.toBitmap()
        mBottomDrawable?.selectDrawable(bottom)
        mBottomBitmap = mBottomDrawable?.toBitmap()
        mLeftDrawable?.selectDrawable(left)
        mLeftBitmap = mLeftDrawable?.toBitmap()
        mRightDrawable?.selectDrawable(right)
        mRightBitmap = mRightDrawable?.toBitmap()
        mLeftTopDrawable?.selectDrawable(leftTop)
        mLeftTopBitmap = mLeftTopDrawable?.toBitmap()
        mRightTopDrawable?.selectDrawable(rightTop)
        mRightTopBitmap = mRightTopDrawable?.toBitmap()
        mLeftBottomDrawable?.selectDrawable(leftBottom)
        mLeftBottomBitmap = mLeftBottomDrawable?.toBitmap()
        mRightBottomDrawable?.selectDrawable(rightBottom)
        mRightBottomBitmap = mRightBottomDrawable?.toBitmap()
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
     * @param angle 点击角度
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
                Log.d("ljm", "mCallBackMode===>> " + mCallBackMode)
                Log.d("ljm", "mDirectionMode==>>>" + mDirectionMode)
                Log.d("ljm", "angle===>> " + angle)
                when (mDirectionMode) {
                    DirectionMode.DIRECTION_2_HORIZONTAL -> if ((ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                        tempDirection = Direction.DIRECTION_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        drawDirectionKey2Horizontal(1 ,0)
                    } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                        tempDirection = Direction.DIRECTION_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        drawDirectionKey2Horizontal(0 ,1)
                    }
                    DirectionMode.DIRECTION_2_VERTICAL -> if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                        tempDirection = Direction.DIRECTION_DOWN
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        drawDirectionKey2Vertical(1, 0)
                    } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP) {
                        tempDirection = Direction.DIRECTION_UP
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        drawDirectionKey2Vertical(0, 1)
                    }
                    DirectionMode.DIRECTION_4_ROTATE_0 -> if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                        // 右下
                        tempDirection = Direction.DIRECTION_DOWN_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        drawDirectionKey4Rotate0Bitmap(1, 1, 1, 0)
                    } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                        // 左下
                        tempDirection = Direction.DIRECTION_DOWN_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        drawDirectionKey4Rotate0Bitmap(1, 1, 0, 1)
                    } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                        // 左上
                        tempDirection = Direction.DIRECTION_UP_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        drawDirectionKey4Rotate0Bitmap(0, 1, 1, 1)
                    } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                        // 右上
                        tempDirection = Direction.DIRECTION_UP_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        drawDirectionKey4Rotate0Bitmap(1, 0, 1, 1)
                    }
                    DirectionMode.DIRECTION_4_ROTATE_45 -> if ((ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                        // 右
                        tempDirection = Direction.DIRECTION_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        drawDirectionKey4Rotate45Bitmap(1, 1, 1, 0)
                    } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                        // 下
                        tempDirection = Direction.DIRECTION_DOWN
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        drawDirectionKey4Rotate45Bitmap(1, 0, 1, 1)
                    } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                        // 左
                        tempDirection = Direction.DIRECTION_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        drawDirectionKey4Rotate45Bitmap(1, 1, 0, 1)
                    } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP) {
                        // 上
                        tempDirection = Direction.DIRECTION_UP
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        drawDirectionKey4Rotate45Bitmap(0, 1, 1, 1)
                    }
                    DirectionMode.DIRECTION_8 -> if ((ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                        tempDirection = Direction.DIRECTION_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_RIGHT)
                        drawDirectionKey8Bitmap(1, 1, 1, 0, 1, 1, 1, 1)
                    } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                        tempDirection = Direction.DIRECTION_DOWN_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_RIGHT)
                        drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 1, 1, 0)
                    } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                        tempDirection = Direction.DIRECTION_DOWN
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN)
                        drawDirectionKey8Bitmap(1, 0, 1, 1, 1, 1, 1, 1)
                    } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                        tempDirection = Direction.DIRECTION_DOWN_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_DOWN_LEFT)
                        drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 1, 0, 1)
                    } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                        tempDirection = Direction.DIRECTION_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_LEFT)
                        drawDirectionKey8Bitmap(1, 1, 0, 1, 1, 1, 1, 1)
                    } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                        tempDirection = Direction.DIRECTION_UP_LEFT
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_LEFT)
                        drawDirectionKey8Bitmap(1, 1, 1, 1, 0, 1, 1, 1)
                    } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && tempDirection != Direction.DIRECTION_UP) {
                        tempDirection = Direction.DIRECTION_UP
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP)
                        drawDirectionKey8Bitmap(0, 1, 1, 1, 1, 1, 1, 1)
                    } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                        tempDirection = Direction.DIRECTION_UP_RIGHT
                        mOnShakeListener!!.direction(Direction.DIRECTION_UP_RIGHT)
                        drawDirectionKey8Bitmap(1, 1, 1, 1, 1, 0, 1, 1)
                    }
                    else -> {
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
     * 方向支持
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
     * 添加点击角度的监听
     *
     * @param listener 回调接口
     */
    fun setOnAngleChangeListener(listener: OnAngleChangeListener?) {
        mOnAngleChangeListener = listener
    }

    /**
     * 添加点击的监听
     *
     * @param directionMode 监听的方向
     * @param listener      回调
     */
    fun setOnShakeListener(directionMode: DirectionMode?, listener: OnShakeListener?) {
        mDirectionMode = directionMode
        mOnShakeListener = listener
    }

    /**
     * 点击方向监听接口
     */
    interface OnShakeListener {
        // 开始
        fun onStart()

        /**
         * 点击方向
         *
         * @param direction 方向
         */
        fun direction(direction: Direction?)

        // 结束
        fun onFinish()
    }

    /**
     * 点击角度的监听接口
     */
    interface OnAngleChangeListener {
        // 开始
        fun onStart()

        /**
         * 点击角度变化
         *
         * @param angle 角度[0,360)
         */
        fun angle(angle: Double)

        // 结束
        fun onFinish()
    }
}