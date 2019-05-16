package com.redugsi.circleprogress

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class CircleProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var loadingPaint: Paint = Paint()
    var barPaint: Paint = Paint()
    var textPaint: Paint = TextPaint()

    var circleLoadingBounds: RectF = RectF()
    var circleBarBounds: RectF = RectF()

    var stroke: Int = 0

    var loadingColor = Color.CYAN
    var barColor = Color.GRAY

    var textColor = Color.WHITE
    var textFontFamily = textPaint.typeface
    var textFontSize = 0F
    var text: String = "%0"

    var progress = 0F

    var showAsInt: Boolean = false

    init {
        readParams(attrs)
        setPaints()
    }

    fun readParams(attrs: AttributeSet?){
        context.obtainStyledAttributes(attrs, R.styleable.CircleProgress).apply {
            stroke = this.getInt(R.styleable.CircleProgress_stroke, 1)
            loadingColor = this.getInt(R.styleable.CircleProgress_loadingColor, Color.CYAN)
            barColor = this.getInt(R.styleable.CircleProgress_barColor, Color.GRAY)
            textColor = this.getInt(R.styleable.CircleProgress_textColor, Color.BLACK)
            textFontSize = this.getDimension(R.styleable.CircleProgress_textSize, 60F)
            showAsInt = this.getBoolean(R.styleable.CircleProgress_showAsInteger, false)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = resolveSize(widthSize, widthMeasureSpec)
        var height = resolveSize(heightSize, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setBounds(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawProgressBar(canvas)
        drawProgressText(canvas)
    }

    fun drawProgressBar(canvas: Canvas?){
        canvas?.drawArc(circleBarBounds, 0F, 360.0F, false, barPaint)
        canvas?.drawArc(circleLoadingBounds, 0F, progress, false, loadingPaint)
    }

    fun drawProgressText(canvas: Canvas?){
        var textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        var textHeight = textBounds.height()
        var textWidth = textPaint.measureText(text)
        canvas?.drawText(text, circleBarBounds.centerX() - (textWidth * 0.5F), circleBarBounds.centerY() + (textHeight * 0.5F), textPaint)
    }

    fun setPaints(){
        loadingPaint.apply {
            style = Paint.Style.STROKE
            color = loadingColor
            strokeWidth = stroke.toFloat()
        }

        barPaint.apply {
            style = Paint.Style.STROKE
            color = barColor
            strokeWidth = stroke.toFloat()
        }

        textPaint.apply {
            textSize = textFontSize
            textFontFamily = textFontFamily
        }
    }

    fun setBounds(w: Int, h: Int){
        var minVal = Math.min(w, h)
        var left = ((w/2) - (minVal / 2) + stroke/2).toFloat()
        var top = ((h/2) - (minVal / 2) + stroke/2).toFloat()
        var right = ((w/2) + (minVal / 2)- stroke/2).toFloat()
        var bottom = ((h/2) + (minVal / 2)- stroke/2).toFloat()

        circleBarBounds.set(left, top, right, bottom)
        circleLoadingBounds.set(left, top, right, bottom)

    }

    fun setProgressValue(value: Float){
        var progressValue = value
        if (progressValue < 0) progressValue = 0F else if(progressValue > 100) progressValue = 100F
        var newProgress = 360.0F * progressValue / 100F
        if (newProgress > 360.0F || newProgress == progress) return
        progress = newProgress
        if (showAsInt) text = "%${progressValue.toInt()}" else text = "%$progressValue"
        invalidate()
    }

}