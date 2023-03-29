package com.example.myapplication.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.myapplication.R
import java.lang.RuntimeException

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 *
 * PasswordView
 * @author liujianming
 * @date 2021-12-29
 */
@SuppressLint("AppCompatCustomView")
class PasswordView(context: Context, attrs: AttributeSet?) :
    EditText(context, attrs) {

    var eye: Drawable
    var eyeWithStrike: Drawable
    var alphaEnabled: Int
    var alphaDisabled: Int
    var visible = false
    var useStrikeThrough = false
    var drawableClick: Boolean = false

    companion object {
        const val ALPHA_ENABLED_DARK = .54f
        const val ALPHA_DISABLED_DARK = .38f
        const val ALPHA_ENABLED_LIGHT = 1f
        const val ALPHA_DISABLED_LIGHT = .5f
    }


    init {
        if (attrs != null) {
            var a: TypedArray = getContext().theme.obtainStyledAttributes(attrs, R.styleable.PasswordView,
                0, 0)
            try {
                useStrikeThrough = a.getBoolean(R.styleable.PasswordView_useStrikeThrough, false)
                visible = a.getBoolean(R.styleable.PasswordView_visible, false)
            } finally {
                a.recycle()
            }
        }

        var enabledColor = ContextCompat.getColor(context, R.color.textColor)
        var isIconDark = isDark(enabledColor)
        alphaEnabled =
            (255 * if (isIconDark) ALPHA_ENABLED_DARK else ALPHA_ENABLED_LIGHT).toInt()
        alphaDisabled =
            (255 * if (isIconDark) ALPHA_DISABLED_DARK else ALPHA_DISABLED_LIGHT).toInt()
        eye = getToggleDrawable(getContext(), R.drawable.ic_passwordvisible, enabledColor)!!
        eyeWithStrike = getToggleDrawable(getContext(), R.drawable.ic_passwordhide, enabledColor)!!
        eyeWithStrike.alpha = alphaEnabled
        setup()
    }

    private fun resolveAttr(@AttrRes attrRes: Int): Int {
        val ta = TypedValue()
        context.theme.resolveAttribute(attrRes, ta, true)
        return ContextCompat.getColor(context, ta.resourceId)
    }

    private fun getToggleDrawable(
        context: Context,
        @DrawableRes drawableResId: Int,
        @ColorInt tint: Int
    ): Drawable? {
        val drawable: Drawable =
            getVectorDrawableWithIntrinsicBounds(context, drawableResId)!!.mutate()
        DrawableCompat.setTint(drawable, tint)
        return drawable
    }

    private fun getVectorDrawableWithIntrinsicBounds(
        context: Context,
        @DrawableRes drawableResId: Int
    ): Drawable? {
        val drawable = getVectorDrawable(context, drawableResId)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        return drawable
    }

    private fun getVectorDrawable(context: Context, @DrawableRes drawableResId: Int): Drawable {
        return try {
            ContextCompat.getDrawable(context, drawableResId)!!
        } catch (e: Resources.NotFoundException) {
            VectorDrawableCompat.create(context.resources, drawableResId, context.theme)!!
        }
    }

    private fun setup() {
        val start = selectionStart
        val end = selectionEnd
        inputType =
            InputType.TYPE_CLASS_TEXT or if (visible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
        setSelection(start, end)
        val drawable = if (useStrikeThrough && !visible) eyeWithStrike else eye
        val drawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3])
        eye.alpha = if (visible && !useStrikeThrough) alphaEnabled else alphaDisabled
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val drawableRightX = width - paddingRight
        val drawableLeftX = drawableRightX - compoundDrawables[2].bounds.width()
        val eyeClicked = event!!.x >= drawableLeftX && event.x <= drawableRightX

        if (event.action == MotionEvent.ACTION_DOWN && eyeClicked) {
            drawableClick = true
            return true
        }

        if (event.action == MotionEvent.ACTION_UP) {
            if (eyeClicked && drawableClick) {
                drawableClick = false
                visible = !visible
                setup()
                invalidate()
                return true
            } else {
                drawableClick = false
            }
        }

        return super.onTouchEvent(event)
    }

    override fun setInputType(type: Int) {
        var typeface = getTypeface()
        super.setInputType(type)
        setTypeface(typeface)
    }

    override fun setError(error: CharSequence?) {
        throw RuntimeException("Please use TextInputLayout.setError() instead!")
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        throw RuntimeException("Please use TextInputLayout.setError() instead!")
    }

    @JvmName("setUseStrikeThrough1")
    fun setUseStrikeThrough(useStrikeThrough: Boolean) {
        this.useStrikeThrough = useStrikeThrough
    }

    private fun isDark(hsl: FloatArray): Boolean {
        return hsl[2] < 0.5f
    }

    private fun isDark(@ColorInt color: Int): Boolean {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        return isDark(hsl)
    }



}