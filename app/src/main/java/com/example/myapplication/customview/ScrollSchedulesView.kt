package com.example.myapplication.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import java.util.ArrayList

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * ScrollSchedulesView
 * @author liujianming
 * @date 2022-02-11
 */
class ScrollSchedulesView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mContext: Context? = null
    protected val mColumnTitle: MutableList<ExcelTitle> = ArrayList()
    protected val mRowTitle: MutableList<ExcelTitle> = ArrayList()

    private val mTitlePaint: Paint by lazy(LazyThreadSafetyMode.NONE) { Paint() }
    private val mBackPaint: Paint by lazy(LazyThreadSafetyMode.NONE) { Paint() }
    private val mLinePaint: Paint by lazy(LazyThreadSafetyMode.NONE) { Paint() }
    private val mSpanPaint: Paint by lazy(LazyThreadSafetyMode.NONE) { Paint() }

    private lateinit var mSpans: Array<IntArray>

    private lateinit var mScheduleGestureDetector: GestureDetector
    private lateinit var mScrollGestureDetector: GestureDetector
    private var mScroller: Scroller? = null

    @ColorInt
    private var columnTitleColor = 0
    @ColorInt
    private var rowTitleColor = 0
    @ColorInt
    private var lineColor = 0
    @ColorInt
    private var columnTitleBackColor = 0

    @ColorInt
    private var scheduleColor = 0

    @ColorInt
    private var dividerColor = 0

    private var columnTitleTextSize = 0f
    private var rowTitleTextSize = 0f
    private var spanPercent = 0f

    init {
        mContext = context
        // 关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val array = mContext?.obtainStyledAttributes(attrs, R.styleable.ScrollSchedulesView)
        columnTitleColor =
            array?.getColor(R.styleable.ScrollSchedulesView_columnTitleTextColor, Color.BLACK)!!
        rowTitleColor = array?.getColor(R.styleable.ScrollSchedulesView_rowTitleTextColor, Color.BLACK)
        lineColor = array?.getColor(R.styleable.ScrollSchedulesView_scheduleLineColor, Color.GRAY)
        columnTitleBackColor = array?.getColor(R.styleable.ScrollSchedulesView_columnTitleBackground, Color.WHITE)
        scheduleColor = array?.getColor(R.styleable.ScrollSchedulesView_scheduleColor, Color.BLUE)
        dividerColor = array?.getColor(R.styleable.ScrollSchedulesView_dividerColor, Color.BLACK)
        columnTitleTextSize = array?.getDimensionPixelSize(R.styleable.ScrollSchedulesView_columnTitleTextSize, 14)?.toFloat()
        rowTitleTextSize = array?.getDimensionPixelSize(R.styleable.ScrollSchedulesView_rowTitleTextSize, 14)?.toFloat()
        val percent = array?.getFloat(R.styleable.ScrollSchedulesView_spanPercent, 0.5f)
        spanPercent = if (percent < 0) 0F else (if (percent > 1) 1 else percent) as Float
        array.recycle()
        init()
    }

    private fun init() {
        // 初始化画笔
        mTitlePaint!!.isAntiAlias = true
        mTitlePaint!!.textAlign = Paint.Align.CENTER

        mLinePaint.color = lineColor
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = 2f

        mBackPaint.color = columnTitleBackColor
        mBackPaint.isAntiAlias = true

        mSpanPaint.flags = Paint.ANTI_ALIAS_FLAG
        mSpanPaint.color = scheduleColor

        setSpanSize(SimpleSpanSize())
        setDefaultTitle()

        //　计划手势检测
        mScheduleGestureDetector = GestureDetector(context, SimpleGestureDetector())
        // 滑动检测
        mScrollGestureDetector = GestureDetector(context, SimpleScrollGestureDetector())
        //　粘性滑动
        mScroller = Scroller(context)
    }

    private fun setDefaultTitle() {
        clearColumnTitle()
            .addColumnTitle("S")
            .addColumnTitle("M")
            .addColumnTitle("T")
            .addColumnTitle("W")
            .addColumnTitle("T")
            .addColumnTitle("F")
            .addColumnTitle("S")
        val rowTitle = arrayOf(
            "AM00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "PM00",
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"
        )
        clearRowTitle()
        for (row in rowTitle) {
            addRowTitle(row)
        }
        commit()
    }

    private fun commit() {
        setupSpan()
        invalidate()
    }

    private fun setupSpan() {
        mSpans = Array(getSpanColumnCount()) { IntArray(getSpanRowCount()) }
    }

    private fun clearColumnTitle(): ScrollSchedulesView {
        mColumnTitle.clear()
        return this
    }

    private fun clearRowTitle(): ScrollSchedulesView? {
        mRowTitle.clear()
        return this
    }

    fun newTitle(): ExcelTitle? {
        return ExcelTitle()
    }

    fun addColumnTitle(title: ExcelTitle) {
        mColumnTitle.add(title)
    }

    fun addRowTitle(title: ExcelTitle) {
        mRowTitle.add(title)
    }

    private fun addColumnTitle(title: String?): ScrollSchedulesView {
        mColumnTitle.add(ExcelTitle(title, columnTitleColor, columnTitleTextSize))
        return this
    }

    private fun addRowTitle(title: String?): ScrollSchedulesView? {
        mRowTitle.add(ExcelTitle(title, rowTitleColor, rowTitleTextSize))
        return this
    }

    /**
     * 要求刷新计划表
     */
    fun callRefreshSchedule(baseScheduleSpanLoading: BaseScheduleSpanLoading?) {
        baseScheduleSpanLoading?.onLoadStart(mSpans)
        for (i in mSpans.indices) {
            for (j in mSpans[i].indices) {
                if (null != baseScheduleSpanLoading) {
                    setSpanStatus(i, j, baseScheduleSpanLoading.getSchedule(i, j))
                }
            }
        }
        baseScheduleSpanLoading?.onLoadFinish(mSpans)
        invalidate()
    }


    fun setUpSpanData(spanData: Array<IntArray>) {
        mSpans = spanData
        invalidate()
    }

    private fun getColumnCount(): Int {
        return getSpanColumnCount() + 1
    }

    private fun getRowCount(): Int {
        return getSpanRowCount() + 1
    }

    private fun getSpanColumnCount(): Int {
        return mColumnTitle.size
    }

    private fun getSpanRowCount(): Int {
        return mRowTitle.size
    }

    private fun getViewDefaultSize(size: Int, measureSpec: Int): Int {
        val measureMode = MeasureSpec.getMode(measureSpec)
        val measureSize = MeasureSpec.getSize(measureSpec)
        var result = size
        when (measureMode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> result = measureSize
            MeasureSpec.UNSPECIFIED -> result = if (size > 0) size else measureSize / 2
            else -> {
            }
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getViewDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getViewDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {

        // 绘制标题
        drawRowTitle(canvas)
        // 绘制横线
        drawHorizontalLine(canvas)
        drawVerticalLine(canvas)

        // 绘制单元格区域
        drawSpan(canvas)

        // 在最后绘制,达到悬浮效果
        drawColumnTitle(canvas)
    }

    private fun drawSpan(canvas: Canvas) {
        val spanWidth = mSpanSize!!.getSpanWidth(this, getColumnCount())
        val spanHeight = mSpanSize!!.getSpanHeight(this, getRowCount())
        val left: Float = paddingLeft.toFloat()
        val top: Float = paddingTop.toFloat()
        val tempSpanBound = RectF(left, top, left + spanWidth, top + spanHeight)
        var startRow = -1
        var endRow = -1
        var startSpanBound: RectF? = null
        var endSpanBound: RectF? = null
        // 列
        for (i in mSpans.indices) {
            tempSpanBound.offset(spanWidth, 0f)
            // 行
            for (j in 0 until mSpans[i].size + 1) {
                tempSpanBound.offset(0f, spanHeight)
                // 单元格是否计划
                val schedule = hasSpanSchedule(i, j)
                if (schedule && j < mSpans[i].size) {
                    // 记录这一段计划的开始行
                    if (startRow == -1) {
                        startRow = j
                        startSpanBound = RectF(tempSpanBound)
                    }
                } else {
                    // 这一段计划的结束
                    if (startRow != -1) {
                        // 记录这一段计划的结束行
                        endRow = j - 1
                        endSpanBound = RectF(tempSpanBound)
                    }
                }
                if (tempSpanBound.bottom - scrollY < top + spanHeight) {
                    if (startRow != -1) {
                        startSpanBound!!.top = top + scrollY
                        startSpanBound.bottom = startSpanBound.top + spanHeight
                    }
                    if (endRow != -1) {
                        startRow = -1
                        endRow = -1
                        startSpanBound = null
                        endSpanBound = null
                    }
                    continue
                }
                if (startRow != -1 && endRow != -1) {
                    onDrawSpan(canvas, mSpanPaint, startRow == endRow, startSpanBound, endSpanBound)
                    // 绘制完当前计划段,重置开始行,重新记录
                    startRow = -1
                    endRow = -1
                    startSpanBound = null
                    endSpanBound = null
                }
            }
            startRow = -1
            endRow = -1
            startSpanBound = null
            endSpanBound = null
            tempSpanBound[tempSpanBound.left, top, tempSpanBound.right] = top + spanHeight
        }
    }

    private fun onDrawSpan(
        canvas: Canvas,
        paint: Paint,
        same: Boolean,
        startSpanBound: RectF?,
        endSpanBound: RectF?
    ) {
        if (same) {
            val scheduleWidth = startSpanBound!!.width() * spanPercent
            val left = startSpanBound.left + (startSpanBound.width() - scheduleWidth) * 0.5f
            val top = startSpanBound.top
            val right = left + scheduleWidth
            val bottom = top + startSpanBound.height()
            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                (right - left) * 0.5f,
                (bottom - top) * 0.5f,
                paint
            )
        } else if (endSpanBound!!.top - startSpanBound!!.top < startSpanBound.width() * spanPercent) {
            if (endSpanBound.top - startSpanBound.top > mSpanSize!!.getSpanHeight(
                    this,
                    getRowCount()
                )
            ) {
                val scheduleWidth = endSpanBound.width() * spanPercent
                val left = startSpanBound.left + (startSpanBound.width() - scheduleWidth) * 0.5f
                val bottom = endSpanBound.top
                val top = bottom - endSpanBound.height()
                val right = left + scheduleWidth
                canvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    (right - left) * 0.5f,
                    (bottom - top) * 0.5f,
                    paint
                )
            }
        } else {
            val spanWidth = mSpanSize!!.getSpanWidth(this, getColumnCount())
            val strokeWidth = spanWidth * spanPercent
            paint.strokeWidth = strokeWidth
            paint.strokeCap = Paint.Cap.ROUND
            val startX = startSpanBound.centerX()
            val startY = startSpanBound.top
            val stopY = endSpanBound.top
            canvas.drawLine(
                startX,
                startY + strokeWidth * 0.5f,
                startX,
                stopY - strokeWidth * 0.5f,
                paint
            )
        }
    }

    /**
     * 绘制纵向横线
     */
    private fun drawVerticalLine(canvas: Canvas) {
        val columnCount = getColumnCount()
        val rowCount = getRowCount()
        val spanWidth = mSpanSize!!.getSpanWidth(this, columnCount)
        val spanHeight = mSpanSize!!.getSpanHeight(this, rowCount)
        // 参照点
        val referX: Float = paddingLeft.toFloat()
        var startX: Float
        var stopX: Float
        val startY: Float = paddingTop + spanHeight + scrollY
        val stopY: Float = startY + getSpanRowCount() * spanHeight - scrollY
        mLinePaint.color = lineColor
        for (i in 0..columnCount) {
            stopX = referX + spanWidth * i
            startX = stopX
            canvas.drawLine(startX, startY, stopX, stopY, mLinePaint)
        }
    }

    /**
     * 绘制横向横线
     */
    private fun drawHorizontalLine(canvas: Canvas) {
        val rowCount = getRowCount()
        val columnCount = getColumnCount()
        val spanHeight = mSpanSize!!.getSpanHeight(this, rowCount)
        val spanWidth = mSpanSize!!.getSpanWidth(this, columnCount)
        // 参照点
        val referY: Float = 0 + paddingTop + spanHeight
        var startY: Float
        var stopY: Float
        val startX: Float = paddingLeft.toFloat()
        val stopX = startX + columnCount * spanWidth
        for (i in 0..getSpanRowCount()) {
            stopY = referY + spanHeight * i
            startY = stopY
            if (startY - scrollY < referY) {
                continue
            }
            var excelTitle: ExcelTitle? = null
            if (i < mRowTitle.size) {
                excelTitle = mRowTitle[i]
            }
            if (null == excelTitle || !excelTitle.isDivider) {
                mLinePaint.color = lineColor
                mLinePaint.strokeWidth = 2f
            } else {
                mLinePaint.strokeWidth = 3f
                mLinePaint.color = if (excelTitle.dividerColor == 0) dividerColor else excelTitle.dividerColor
            }
            canvas.drawLine(startX, startY, stopX, stopY, mLinePaint)
        }
    }

    private fun drawRowTitle(canvas: Canvas) {
        val spanHeight = mSpanSize!!.getSpanHeight(this, getRowCount())
        val startY: Float = paddingTop.toFloat()
        val startX: Float = paddingLeft.toFloat()
        val tempBound = RectF(
            startX,
            startY,
            startX + mSpanSize!!.getSpanWidth(this, getColumnCount()),
            startY + spanHeight
        )
        for (i in mRowTitle.indices) {
            tempBound.offset(0f, spanHeight)
            if (tempBound.bottom - scrollY < startY + spanHeight) {
                continue
            }
            val excelTitle = mRowTitle[i]
            val title = excelTitle.title
            if (!TextUtils.isEmpty(title)) {
                mTitlePaint!!.color =
                    if (excelTitle.textColor == 0) rowTitleColor else excelTitle.textColor

                //1 进行字体大小的合适缩放
                val defaultSize =
                    if (excelTitle.textSize == 0f) rowTitleTextSize else excelTitle.textSize
                val length = title!!.length
                var position = title.length - 2
                if (position < 2) {
                    position = 0
                }
                val fontScale = getFontScale(context)
                var width: Int //用来记录测量出来的字体宽度
                val maxWidth: Int = getWidth() / getColumnCount()

                //加1，为了第一次的textSize为mMaxTextSize
                var textSize = defaultSize + 1f

                //用来记录测量出来left、top、right、bottom
                val rect = Rect()
                val rect1 = Rect()
                do {
                    textSize -= 1f
                    mTitlePaint!!.textSize = textSize * fontScale

                    //计算大字体的边界
                    mTitlePaint!!.getTextBounds(title, 0, position, rect)
                    mTitlePaint!!.textSize = textSize * fontScale

                    //计算小字体的边界
                    mTitlePaint!!.getTextBounds(title, position, length, rect1)

                    //测量出来的宽度
                    width = rect.right - rect.left + (rect1.right - rect1.left)
                } while (width > maxWidth)

                //2 居中
                val fontMetrics = mTitlePaint!!.fontMetrics
                val textH = fontMetrics.bottom - fontMetrics.top
                val r = fontMetrics.bottom / textH
                val baseline = tempBound.centerY() + (textH * 0.5f - textH * r)
                canvas.drawText(title, tempBound.centerX(), baseline, mTitlePaint!!)
            }
        }
    }


    /**
     * 获取字体缩放比例
     */
    private fun getFontScale(context: Context): Float {
        return context.resources.displayMetrics.scaledDensity
    }

    private fun drawColumnTitle(canvas: Canvas) {
        val spanWidth = mSpanSize!!.getSpanWidth(this, getColumnCount())
        val spanHeight = mSpanSize!!.getSpanHeight(this, getRowCount())
        val startX: Float = paddingLeft + spanWidth
        val startY: Float = (scrollY + paddingTop).toFloat()
        mBackPaint.color = columnTitleBackColor
        // 绘制背景
        canvas.drawRect(
            paddingLeft.toFloat(),
            startY,
            startX + spanWidth * getColumnCount(),
            startY + spanHeight,
            mBackPaint
        )
        mLinePaint.color = lineColor
        canvas.drawLine(
            paddingLeft.toFloat(),
            startY,
            startX + spanWidth * getColumnCount(),
            startY,
            mLinePaint
        )
        mLinePaint.color = dividerColor
        canvas.drawLine(
            paddingLeft.toFloat(),
            startY + spanHeight,
            startX + spanWidth * getColumnCount(),
            startY + spanHeight,
            mLinePaint
        )
        val tempBound = RectF(startX, startY, startX + spanWidth, startY + spanHeight)
        mLinePaint.color = lineColor
        canvas.drawLine(
            paddingLeft.toFloat(),
            tempBound.top,
            paddingLeft.toFloat(),
            tempBound.bottom,
            mLinePaint
        )
        for (i in mColumnTitle.indices) {
            val excelTitle = mColumnTitle[i]
            val title = excelTitle.title
            if (!TextUtils.isEmpty(title)) {
                mTitlePaint!!.color =
                    if (excelTitle.textColor == 0) columnTitleColor else excelTitle.textColor

                //1 进行字体大小的合适缩放
                val defaultSize =
                    if (excelTitle.textSize == 0f) columnTitleTextSize else excelTitle.textSize
                val length = title!!.length
                var position = title.length - 2
                if (position < 2) {
                    position = 0
                }
                val fontScale = getFontScale(context)
                var width: Int //用来记录测量出来的字体宽度
                val maxWidth: Int = getWidth() / getColumnCount()

                //加1，为了第一次的textSize为mMaxTextSize
                var textSize = defaultSize + 1f

                //用来记录测量出来left、top、right、bottom
                val rect = Rect()
                val rect1 = Rect()
                do {
                    textSize -= 1f
                    mTitlePaint!!.textSize = textSize * fontScale
                    //计算大字体的边界
                    mTitlePaint!!.getTextBounds(title, 0, position, rect)
                    mTitlePaint!!.textSize = textSize * fontScale
                    //计算小字体的边界
                    mTitlePaint!!.getTextBounds(title, position, length, rect1)
                    //测量出来的宽度
                    width = rect.right - rect.left + (rect1.right - rect1.left)
                } while (width > maxWidth)

                //2 居中位置
                val fontMetrics = mTitlePaint!!.fontMetrics
                val textH = fontMetrics.bottom - fontMetrics.top
                val r = fontMetrics.bottom / textH
                val baseline = tempBound.centerY() + (textH * 0.5f - textH * r)
                canvas.drawText(title, tempBound.centerX(), baseline, mTitlePaint!!)
            }
            canvas.drawLine(
                tempBound.left,
                tempBound.top,
                tempBound.left,
                tempBound.bottom,
                mLinePaint
            )
            tempBound.offset(spanWidth, 0f)
        }
        canvas.drawLine(tempBound.left, tempBound.top, tempBound.left, tempBound.bottom, mLinePaint)
    }

    /**
     * 根据x坐标获取对应的列(相对于整个计划表)
     */
    protected fun getColumnByX(x: Float): Int {
        return (x / mSpanSize!!.getSpanWidth(this@ScrollSchedulesView, getColumnCount())).toInt()
    }

    /**
     * 根据x坐标获取对应的列(相对于整个计划表)
     */
    protected fun getRowByY(y: Float): Int {
        return (y / mSpanSize!!.getSpanHeight(this@ScrollSchedulesView, getRowCount())).toInt()
    }

    /**
     * 列是否合法
     */
    private fun legalSpanColumn(column: Int): Boolean {
        return 0 <= column && column < mSpans.size
    }

    /**
     * 行/列是否合法
     */
    fun legalSpanColumnAndRow(column: Int, row: Int): Boolean {
        return legalSpanColumn(column) && 0 <= row && row < mSpans[column].size
    }

    /**
     * 根据传入位置和状态设置span状态
     */
    fun setSpanStatus(column: Int, row: Int, status: Boolean) {
        if (0 <= column && column < mSpans.size) {
            if (0 <= row && row < mSpans[column].size) {
                mSpans[column][row] = if (status) 1 else 0
            }
        }
    }

    /**
     * 根据传入范围,设置部分span状态
     */
    fun setSpanStatus(
        startColumn: Int,
        startRow: Int,
        endColumn: Int,
        endRow: Int,
        status: Boolean
    ) {
        for (i in startColumn..endColumn) {
            for (j in startRow..endRow) {
                setSpanStatus(i, j, status)
            }
        }
    }

    /**
     * 根据传入状态,设置整个计划
     */
    fun setSpanStatus(status: Boolean) {
        for (i in mSpans.indices) {
            for (j in mSpans[i].indices) {
                setSpanStatus(i, j, status)
            }
        }
    }

    /**
     * 是否存在计划
     */
    fun hasSchedule(): Boolean {
        for (i in mSpans.indices) {
            if (hasColumnSchedule(i)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断对应行是否有计划
     */
    fun hasRowSchedule(row: Int): Boolean {
        for (span in mSpans) {
            for (j in span.indices) {
                if (span[row] != 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun hasSpanSchedule(column: Int, row: Int): Boolean {
        if (0 <= column && column < mSpans.size) {
            if (0 <= row && row < mSpans[column].size) {
                return mSpans[column][row] != 0
            }
        }
        return false
    }

    /**
     * 判断传入的列是否有计划
     */
    fun hasColumnSchedule(column: Int): Boolean {
        if (0 <= column && column < mSpans.size) {
            for (element in mSpans[column]) {
                if (element != 0) {
                    return true
                }
            }
        }
        return false
    }

    /***
     * 根据传入的参数,自行判断开始和结束值
     * 返回一个 rect -- left 表示开始列,top 表示开始行,right 表示结束列,bottom 表示结束行
     */
    private fun getSpanRangeRect(downColumn: Int, downRow: Int, curColumn: Int, curRow: Int): Rect {
        val startRow: Int
        val startColumn: Int
        val endRow: Int
        val endColumn: Int
        if (downRow > curRow) {
            startRow = curRow
            endRow = downRow
        } else {
            startRow = downRow
            endRow = curRow
        }
        if (downColumn > curColumn) {
            startColumn = curColumn
            endColumn = downColumn
        } else {
            startColumn = downColumn
            endColumn = curColumn
        }
        return Rect(startColumn, startRow, endColumn, endRow)
    }

    private fun getFlingMaxY(): Int {
        return ((mSpanSize!!.getSpanHeight(
            this,
            getRowCount()
        ) * getRowCount() - (height - paddingTop - paddingBottom)).toInt())
    }

    protected fun reFreshSchedule() {
        invalidate()
        if (null != onScheduleChangeListener) {
            onScheduleChangeListener!!.onScheduleChange(mSpans)
        }
    }

    inner class SimpleScrollGestureDetector : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            // 只处理多指触摸
            return multiplyPointer
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (scrollY < 0 || scrollY > getFlingMaxY()) {
                return false
            }
            scrollBy(0, distanceY.toInt())
            return true
        }
    }

    inner class SimpleGestureDetector : SimpleOnGestureListener() {
            private var downInVerticalTitle = false
            private var downInHorizontalTitle = false
            private var spanDownColumn = 0
            private var spanDownRow = 0
            private var lastSpanColumn = 0
            private var lastSpanRow = 0
            private var scrollStatus = false

            /**
             * 恢复默认
             */
            private fun reset() {
                spanDownColumn = -1
                spanDownRow = -1
                lastSpanColumn = -1
                lastSpanRow = -1
                scrollStatus = false
                downInHorizontalTitle = false
                downInVerticalTitle = false
            }

            override fun onDown(e: MotionEvent): Boolean {
                reset()
                //  是否按在第一列(相对于整个表)
                downInVerticalTitle = getColumnByX(e.x - paddingLeft) == 0
                // 是否按在第一行(相对于整个表)
                downInHorizontalTitle = getRowByY(e.y - paddingTop) == 0
                // 计算按下的列(相对于计划区)
                spanDownColumn = getColumnByX(scrollX + e.x - paddingLeft) - 1
                lastSpanColumn = spanDownColumn
                // 计算按下的行(相对于计划区)
                spanDownRow = getRowByY(scrollY + e.y - paddingTop) - 1
                lastSpanRow = spanDownRow

                // 只处理单指触摸
                return 1 == e.pointerCount
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {

                // 点击在左上角
                if (downInHorizontalTitle && downInVerticalTitle) {
                    setSpanStatus(!hasSchedule())
                    reFreshSchedule()
                    return true
                    // 点击在时间上
                } else if (downInVerticalTitle) {
                    setSpanStatus(
                        0,
                        spanDownRow,
                        mSpans.size,
                        spanDownRow,
                        !hasRowSchedule(spanDownRow)
                    )
                    reFreshSchedule()
                    return true
                    // 点击在 日期上
                } else if (downInHorizontalTitle) {
                    setSpanStatus(
                        spanDownColumn,
                        0,
                        spanDownColumn,
                        mSpans[spanDownColumn].size,
                        !hasColumnSchedule(spanDownColumn)
                    )
                    reFreshSchedule()
                    return true
                }
                if (!legalSpanColumnAndRow(spanDownColumn, spanDownRow)) {
                    return false
                }
                val downStatus = mSpans[spanDownColumn][spanDownRow]
                setSpanStatus(spanDownColumn, spanDownRow, downStatus == 0)
                reFreshSchedule()
                return true
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (downInHorizontalTitle || downInVerticalTitle) {
                    if (!scrollStatus) {
                        if (downInHorizontalTitle) {
                            if (spanDownColumn < 0) {
                                return false
                            }
                            setSpanStatus(
                                spanDownColumn,
                                0,
                                spanDownColumn,
                                mSpans[spanDownColumn].size,
                                !hasColumnSchedule(spanDownColumn)
                            )
                        } else {
                            setSpanStatus(
                                0,
                                spanDownRow,
                                mSpans.size,
                                spanDownRow,
                                !hasRowSchedule(spanDownRow)
                            )
                        }
                        reFreshSchedule()
                        scrollStatus = true
                    } else {
                        if (downInHorizontalTitle) {
                            val column = getColumnByX(scrollX + e2.x - paddingLeft) - 1
                            //fixed:列滑动到第0个位置此时应当忽略,否则下面调用出现 ArrayIndexOutOfBoundsException
                            if (column < 0 || lastSpanColumn == column) {
                                return false
                            }
                            val rangeRect =
                                getSpanRangeRect(spanDownColumn, 0, column, mSpans[column].size)
                            setSpanStatus(
                                rangeRect.left,
                                rangeRect.top,
                                rangeRect.right,
                                rangeRect.bottom,
                                hasColumnSchedule(spanDownColumn)
                            )
                            lastSpanColumn = column
                            reFreshSchedule()
                        } else {
                            val row = getRowByY(scrollY + e2.y - paddingTop) - 1
                            if (lastSpanRow == row) {
                                return false
                            }
                            val rangeRect = getSpanRangeRect(0, spanDownRow, mSpans.size, row)
                            setSpanStatus(
                                rangeRect.left,
                                rangeRect.top,
                                rangeRect.right,
                                rangeRect.bottom,
                                hasRowSchedule(spanDownRow)
                            )
                            lastSpanRow = row
                            reFreshSchedule()
                        }
                    }
                    return true
                }
                if (!legalSpanColumnAndRow(spanDownColumn, spanDownRow)) {
                    return false
                }
                val downStatus = mSpans[spanDownColumn][spanDownRow]
                if (!scrollStatus) {
                    setSpanStatus(spanDownColumn, spanDownRow, downStatus == 0)
                    reFreshSchedule()
                    scrollStatus = true
                } else {
                    val column = getColumnByX(scrollX + e2.x - paddingLeft) - 1
                    val row = getRowByY(scrollY + e2.y - paddingTop) - 1
                    if (!legalSpanColumnAndRow(column, row)) {
                        return false
                    }
                    if (lastSpanRow == row && lastSpanColumn == column) {
                        return false
                    }

                    // 按下单元格与当前单元格范围
                    val rangeRect = getSpanRangeRect(spanDownColumn, spanDownRow, column, row)
                    setSpanStatus(
                        rangeRect.left,
                        rangeRect.top,
                        rangeRect.right,
                        rangeRect.bottom,
                        downStatus != 0
                    )
                    reFreshSchedule()
                    lastSpanColumn = column
                    lastSpanRow = row
                }
                return true
            }
        }

    /**
     * 是否多指
     */
    var multiplyPointer = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> multiplyPointer = false
            MotionEvent.ACTION_POINTER_DOWN -> multiplyPointer = true
            else -> {
            }
        }
        val result: Boolean
        val pointerCount = event.pointerCount
        result = if (1 == pointerCount && !multiplyPointer) {
            mScheduleGestureDetector!!.onTouchEvent(event)
        } else {
            // 多指触摸,处理滑动事件
            mScrollGestureDetector!!.onTouchEvent(event)
        }
        if (action == MotionEvent.ACTION_UP) {
            multiplyPointer = false
            var dy = 0
            if (scrollY < 0) {
                dy = -scrollY
            } else if (scrollY > getFlingMaxY()) {
                dy = getFlingMaxY() - scrollY
            }
            if (dy != 0) {
                mScroller!!.startScroll(scrollX, scrollY, -scrollX, dy, 300)
                invalidate()
            }
        }
        return result || super.onTouchEvent(event)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
        }
    }

    fun getExcelWidth(): Float {
        return (width - paddingLeft - paddingRight) * 1.0f
    }

    fun getExcelHeight(): Float {
        return (height - paddingTop - paddingBottom) * 1.0f
    }

    private var mSpanSize: SpanSize? = null

    private fun setSpanSize(spanSize: SpanSize?) {
        mSpanSize = spanSize
        invalidate()
    }

    fun setColumnTitleColor(columnTitleColor: Int) {
        this.columnTitleColor = columnTitleColor
    }

    fun setRowTitleColor(rowTitleColor: Int) {
        this.rowTitleColor = rowTitleColor
    }

    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
    }

    fun setColumnTitleBackColor(columnTitleBackColor: Int) {
        this.columnTitleBackColor = columnTitleBackColor
    }

    fun setSpanColor(@ColorRes color: Int) {
        mSpanPaint.color = ContextCompat.getColor(context, color)
    }

    internal class SimpleSpanSize : SpanSize {
        override fun getSpanWidth(view: ScrollSchedulesView?, columnCount: Int): Float {
            return view!!.getExcelWidth() / columnCount
        }

        override fun getSpanHeight(view: ScrollSchedulesView?, rowCount: Int): Float {
            return view!!.getExcelHeight() / rowCount
        }
    }

    interface SpanSize {
        /**
         * 获取单元格宽度
         *
         * @param view:视图
         * @param columnCount：列数量
         * @return 返回单元格宽度
         */
        fun getSpanWidth(view: ScrollSchedulesView?, columnCount: Int): Float

        /**
         * 获取单元格高度
         *
         * @param view:视图
         * @param rowCount：列数量
         * @return 返回单元格高度
         */
        fun getSpanHeight(view: ScrollSchedulesView?, rowCount: Int): Float
    }

    private var onScheduleChangeListener: OnScheduleChangeListener? = null

    fun setOnScheduleChangeListener(onScheduleChangeListener: OnScheduleChangeListener?) {
        this.onScheduleChangeListener = onScheduleChangeListener
    }

    fun getContexts(): Context {
        return mContext!!
    }

    class ExcelTitle {
        var title: String? = null
            private set

        @ColorInt
        var textColor = 0
            private set
        var textSize = 0f
            private set
        var isDivider = false
            private set

        @ColorInt
        var dividerColor = 0
            private set

        constructor() {}
        constructor(title: String?, textColor: Int, textSize: Float) {
            this.title = title
            this.textColor = textColor
            this.textSize = textSize
        }

        fun setTitle(title: String?): ExcelTitle {
            this.title = title
            return this
        }

        fun setTextColor(@ColorRes textColor: Int): ExcelTitle {
//            this.textColor = ContextCompat.getColor(mContext, textColor)
            return this
        }

        fun setTextSize(textSize: Float): ExcelTitle {
            this.textSize = textSize
            return this
        }

        fun setDivider(divider: Boolean): ExcelTitle {
            isDivider = divider
            return this
        }

        fun setDividerColor(@ColorRes dividerColor: Int): ExcelTitle {
//            this.dividerColor = ContextCompat.getColor(mContext, dividerColor)
            return this
        }
    }

    /**
     * 计划发生变化监听
     */
    interface OnScheduleChangeListener {
        /**
         * 计划监听
         *
         * @param schedule:计划表数据
         */
        fun onScheduleChange(schedule: Array<IntArray>?)
    }

    /**
     * 计划单元格初始化
     */
    abstract class BaseScheduleSpanLoading {
        /**
         * 开始初始化
         *
         * @param schedule:计划表数据
         */
        fun onLoadStart(schedule: Array<IntArray>?) {}

        /**
         * 单元格初始化完成
         *
         * @param schedule:计划表数据
         */
        fun onLoadFinish(schedule: Array<IntArray>?) {}

        /**
         * 获取对应计划表位置
         *
         * @param column:列
         * @param row：行
         * @return 0表示不计划, 否则计划
         */
        abstract fun getSchedule(column: Int, row: Int): Boolean
    }
}