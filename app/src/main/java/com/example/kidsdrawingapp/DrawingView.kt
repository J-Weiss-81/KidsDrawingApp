package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //Figure out all the variables that are needed, reference here if you need to add or change var.
    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    //Set up the variables for the "paint"
    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

   //Setting up the canvas as a bitmap
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //This is the setup to be able to draw on the canvas
    //if Canvas fails switch to nullable Canvas?
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)

        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        if (event != null) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDrawPath!!.color = color
                    mDrawPath!!.brushThickness = mBrushSize

                    mDrawPath!!.reset()
                    if (touchX != null) {
                        if (touchY != null) {
                            mDrawPath!!.moveTo(touchX, touchY)
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (touchX != null) {
                        if (touchY != null) {
                            mDrawPath!!.lineTo(touchX, touchY)
                        }
                    }

                }
                MotionEvent.ACTION_UP ->{
                    mPaths.add(mDrawPath!!)
                    mDrawPath = CustomPath(color, mBrushSize)
                }
                else -> return false
            }
        }
        invalidate()

        return true



    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }


}