package hu.netlife.othellopepper.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import dagger.hilt.android.AndroidEntryPoint
import hu.netlife.othellopepper.R
import hu.netlife.othellopepper.game.OthelloModel
import javax.inject.Inject

@AndroidEntryPoint
class OthelloView: View {
    private val paintBg = Paint()
    private val paintLine = Paint()
    private val paintPlayer = Paint()
    private val paintSteps = Paint()

    private lateinit var gameCanvas: Canvas

    private var possibilities = ArrayList<Pair<Int,Int>>()
    private var othelloViewListener: OthelloViewListener? = null

    @Inject
    lateinit var othelloModel: OthelloModel

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    interface OthelloViewListener{
        fun onNextPlayerChanged(nextPlayer: Int)
        fun onGameEnded(winner: Int, white: Int, black: Int)
        fun onPointsChanged(white: Int, black: Int)
    }

    init {
        paintBg.color = ResourcesCompat.getColor(resources, R.color.netlife_color, null)
        paintBg.style = Paint.Style.FILL

        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 10F

        paintPlayer.color = Color.WHITE
        paintPlayer.style = Paint.Style.FILL

        paintSteps.color = Color.GREEN
        paintSteps.style = Paint.Style.STROKE
        paintSteps.strokeWidth = 10F
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBg)
        gameCanvas = canvas
        drawGameArea(canvas)
        drawPlayers(canvas)
        drawPossibleSteps(canvas)

    }

    fun setOthelloViewListener(listener: OthelloViewListener){
        othelloViewListener = listener
    }

    private fun drawPossibleSteps(canvas: Canvas) {
        possibilities = othelloModel.checkPossibleSteps()
        for(pos in possibilities){
            val x = pos.first
            val y= pos.second
            canvas.drawRect((x*width/8).toFloat(), (y*height/8).toFloat(), ((x+1)*width/8).toFloat(), ((y+1)*height/8).toFloat(), paintSteps)
        }
        if(possibilities.isEmpty()){
            othelloModel.changePlayer()
            othelloViewListener?.onNextPlayerChanged(othelloModel.nextPlayer)
            possibilities = othelloModel.checkPossibleSteps()
            for(pos in possibilities){
                val x = pos.first
                val y= pos.second
                canvas.drawRect((x*width/8).toFloat(), (y*height/8).toFloat(), ((x+1)*width/8).toFloat(), ((y+1)*height/8).toFloat(), paintSteps)
            }
            if(possibilities.isEmpty()){
                val points = othelloModel.getPoints()
                val winner =if(points.first>points.second) 1 else if(points.first!=points.second) 2 else 3
                othelloViewListener?.onGameEnded(winner, points.first, points.second)
            }
        }
    }

    private fun drawGameArea(canvas: Canvas) {
        val widthFloat: Float = width.toFloat()
        val heightFloat: Float = height.toFloat()

        // border
        canvas.drawRect(0F, 0F, widthFloat, heightFloat, paintLine)

        // seven horizontal lines
        canvas.drawLine(0F, heightFloat / 8, widthFloat, widthFloat / 8, paintLine)
        canvas.drawLine(0F, 2 * heightFloat / 8, widthFloat, 2 * heightFloat / 8, paintLine)
        canvas.drawLine(0F, 3 * heightFloat / 8, widthFloat, 3 * heightFloat / 8, paintLine)
        canvas.drawLine(0F, 4 * heightFloat / 8, widthFloat, 4 * heightFloat / 8, paintLine)
        canvas.drawLine(0F, 5 * heightFloat / 8, widthFloat, 5 * heightFloat / 8, paintLine)
        canvas.drawLine(0F, 6 * heightFloat / 8, widthFloat, 6 * heightFloat / 8, paintLine)
        canvas.drawLine(0F, 7 * heightFloat / 8, widthFloat, 7 * heightFloat / 8, paintLine)

        // seven vertical lines
        canvas.drawLine(widthFloat / 8, 0F, widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(2 * widthFloat / 8, 0F, 2 * widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(3 * widthFloat / 8, 0F, 3 * widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(4 * widthFloat / 8, 0F, 4 * widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(5 * widthFloat / 8, 0F, 5 * widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(6 * widthFloat / 8, 0F, 6 * widthFloat / 8, heightFloat, paintLine)
        canvas.drawLine(7 * widthFloat / 8, 0F, 7 * widthFloat / 8, heightFloat, paintLine)

    }

    private fun drawPlayers(canvas: Canvas){
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                when (othelloModel.getFieldContent(i, j)) {
                    OthelloModel.WHITE ->{
                        paintPlayer.color = Color.WHITE
                        val centerX = i * width / 8 + width / 16
                        val centerY = j * height / 8 + height / 16
                        val radius = height / 16 - 10
                        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paintPlayer)
                    }
                    OthelloModel.BLACK ->{
                        paintPlayer.color = Color.BLACK
                        val centerX = i * width / 8 + width / 16
                        val centerY = j * height / 8 + height / 16
                        val radius = height / 16 - 10
                        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paintPlayer)
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val d: Int

        when {
            w == 0 -> { d = h }
            h == 0 -> { d = w }
            else -> { d = Math.min(w, h)}
        }
        setMeasuredDimension(d, d)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val tX: Int = (event.x / (width / 8)).toInt()
                val tY: Int = (event.y / (height / 8)).toInt()
                val possible = possibilities.contains(Pair(tX, tY))
                if (tX < 8 && tY < 8 && othelloModel.getFieldContent(tX, tY) == OthelloModel.EMPTY && possible) {
                    othelloModel.setFieldContent(tX, tY, othelloModel.nextPlayer)
                    othelloViewListener?.onNextPlayerChanged(othelloModel.nextPlayer)
                    val points = othelloModel.getPoints()
                    othelloViewListener?.onPointsChanged(points.first, points.second)
                    invalidate()
                }
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }

}
