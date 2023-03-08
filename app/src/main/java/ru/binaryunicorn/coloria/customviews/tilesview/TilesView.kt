package ru.binaryunicorn.coloria.customviews.tilesview

import android.content.Context
import android.graphics.Point
import android.media.SoundPool
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.LinkedList
import java.util.Queue
import java.util.Random
import kotlin.math.ceil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.binaryunicorn.coloria.common.structs.colors.RgbColor
import ru.binaryunicorn.coloria.common.structs.colors.toFloatRgb

/* Отрисовывает указаное количетво прямоугольников, изменяющие цвет по косанию или автоматически (анимация)
 * Самостоятельно восстанавливает свое состояние, кроме звука.
 * Необходимо самостоятельно вызывать updateSounds() и releaseSounds()
 */
class TilesView(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs), View.OnTouchListener {

    var isSoundEnabled = false
    var isRgbMode = false
        set(value) {
            field = value
            if (value) {
                generateField(1, 1)
                nextColorForTile(0, 0)
            } else {
                rgbModeColors = makeRgbColorList()
            }
        }

    private var tilesOnWidth = 0
    private var tilesOnHeight = 0
    private val tapInfos = Array(10) { Point(-1, -1) }
    private var colors: Array<Array<RgbColor>> = arrayOf()
    private var rgbModeColors: Queue<RgbColor> = makeRgbColorList()

    private var animationJob: Job? = null
    private var animationStepDelay = 0L
    private var animationPercentageOfTiles = 0.0f // указывается от 0.0f до 1.0f

    private val renderer = TilesViewRenderer()
    private var soundPool: SoundPool? = null
    private var sounds = intArrayOf()
    private val colorRandom = Random()
    private val soundRandom = Random()
    private val tilesRandom = Random()

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
        setOnTouchListener(this)
    }

    //// View ////

    override fun onSaveInstanceState(): Parcelable =
        Bundle().apply {
            putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
            putInt(KEY_COUNT_OF_HORIZONTAL_TILES, tilesOnWidth)
            putInt(KEY_COUNT_OF_VERTICAL_TILES, tilesOnHeight)
            putBoolean(KEY_SOUND_ENABLED, isSoundEnabled)
            putBoolean(KEY_IS_ANIMATE, (animationJob?.isActive == true))
            putBoolean(KEY_RGB_MODE_ENABLED, isRgbMode)
            putString(KEY_TILE_COLORS, Gson().toJson(colors))
            putString(KEY_RGB_MODE_COLORS, Gson().toJson(rgbModeColors))
            putLong(KEY_ANIMATION_STEP_DELAY, animationStepDelay)
            putFloat(KEY_ANIMATION_PERCENTAGE_OF_TILES, animationPercentageOfTiles)
        }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            tilesOnWidth = state.getInt(KEY_COUNT_OF_HORIZONTAL_TILES)
            tilesOnHeight = state.getInt(KEY_COUNT_OF_VERTICAL_TILES)
            isSoundEnabled = state.getBoolean(KEY_SOUND_ENABLED)
            isRgbMode = state.getBoolean(KEY_RGB_MODE_ENABLED)
            colors = Gson().fromJson(
                state.getString(KEY_TILE_COLORS),
                object : TypeToken<Array<Array<RgbColor>>>() {}.type
            )
            rgbModeColors = Gson().fromJson(
                state.getString(KEY_RGB_MODE_COLORS),
                object : TypeToken<Queue<RgbColor>>() {}.type
            )
            animationStepDelay = state.getLong(KEY_ANIMATION_STEP_DELAY)
            animationPercentageOfTiles = state.getFloat(KEY_ANIMATION_PERCENTAGE_OF_TILES)

            renderer.configure(colors.toFloatRgb(), tilesOnWidth, tilesOnHeight)

            if (state.getBoolean(KEY_IS_ANIMATE)) {
                enableAnimation(animationStepDelay, animationPercentageOfTiles)
            }

            super.onRestoreInstanceState(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    state.getParcelable(KEY_SUPER_STATE, Parcelable::class.java)
                } else {
                    @Suppress("DEPRECATION") state.getParcelable(KEY_SUPER_STATE)
                }
            )
        }
    }

    //// Public ////

    fun generateField(tilesOnWidth: Int, tilesOnHeight: Int) {
        this.tilesOnWidth = tilesOnWidth
        this.tilesOnHeight = tilesOnHeight
        colors =
            Array(tilesOnHeight) {
                Array(tilesOnWidth) {
                    RgbColor(colorRandom.nextInt(255), colorRandom.nextInt(255), colorRandom.nextInt(255))
                }
            }

        renderer.configure(colors.toFloatRgb(), tilesOnWidth, tilesOnHeight)
    }

    fun nextColorForTile(horizontalPosition: Int, verticalPosition: Int) {
        try {
            colors[verticalPosition][horizontalPosition] =
                if (isRgbMode) {
                    val nextColor = rgbModeColors.remove()
                    rgbModeColors.add(nextColor)
                    nextColor
                } else {
                    RgbColor(colorRandom.nextInt(255), colorRandom.nextInt(255), colorRandom.nextInt(255))
                }

            renderer.updateTile(
                horizontalPosition,
                verticalPosition,
                colors[verticalPosition][horizontalPosition].toFloatRgb()
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSounds(soundPool: SoundPool, sounds: IntArray) {
        this.soundPool = soundPool
        this.sounds = sounds
    }

    fun releaseSounds() {
        soundPool?.release()
        soundPool = null
        sounds = intArrayOf()
    }

    fun enableAnimation(stepDelay: Long, percentageOfTilesForStep: Float) {
        if (animationJob?.isActive == true) { animationJob?.cancel() }
        animationStepDelay = stepDelay
        animationPercentageOfTiles = percentageOfTilesForStep.let {
            when {
                it < 0.0f -> { 0.0f }
                it > 1.0f -> { 1.0f }
                else -> it
            }
        }

        animationJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                animateStep()
                delay(animationStepDelay)
            }
        }
    }

    fun disableAnimation() {
        animationJob?.cancel()
    }

    //// View.OnTouchListener ////

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // вычисляем размеры квадратов
        val tileWidth = ceil( width.toDouble() / tilesOnWidth.toDouble() ).toInt()
        val tileHeight = ceil( height.toDouble() / tilesOnHeight.toDouble() ).toInt()

        return if (event == null) {
            false
        } else {
            // косание за границей вью
            if (event.x < 0 || event.x > width || event.y < 0 || event.y > height) {
                return true
            }

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    for (i in 0 until event.pointerCount) {
                        val id = event.getPointerId(i)
                        val horizontalPosition = event.getX(i).toInt() / tileWidth
                        val verticalPosition = event.getY(i).toInt() / tileHeight

                        // если не отпускаем палец и ведем по экрану
                        if (horizontalPosition != tapInfos[id].x || verticalPosition != tapInfos[id].y) {
                            tapInfos[id].x = horizontalPosition
                            tapInfos[id].y = verticalPosition

                            nextColorForTile(horizontalPosition, verticalPosition)

                            if (isSoundEnabled && soundPool != null && sounds.isNotEmpty()) {
                                soundPool?.play(sounds[soundRandom.nextInt(sounds.size)], 1.0f, 1.0f, 0, 0, 1.0f )
                            }
                        }
                    }
                }

                MotionEvent.ACTION_UP -> {
                    for (item in tapInfos) {
                        item.x = -1
                        item.y = -1
                    }
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    val id = event.getPointerId(event.actionIndex)
                    tapInfos[id].x = -1
                    tapInfos[id].y = -1
                }
            }

            invalidate()
            true
        }
    }

    //// Private ////

    private fun animateStep() {
        if (animationPercentageOfTiles >= 0.99f) { // 100%
            for (y in 0 until tilesOnHeight) {
                for (x in 0 until tilesOnWidth) {
                    nextColorForTile(x, y)
                }
            }
        } else {
            val countToChange = (tilesOnWidth * tilesOnHeight) * animationPercentageOfTiles
            for (i in 0 until countToChange.toInt()) {
                // одна и таже плитка может перекрашиваться несколько раз, это хорошо - обеспечиваем "случайность" количества
                nextColorForTile(tilesRandom.nextInt(tilesOnWidth), tilesRandom.nextInt(tilesOnHeight))
            }
        }

        invalidate()
    }

    private fun makeRgbColorList(): Queue<RgbColor> {
        return LinkedList(
            listOf(
                RgbColor(255, 0, 0),
                RgbColor(0, 255, 0),
                RgbColor(0, 0, 255),
                RgbColor(255, 255, 255)
            )
        )
    }

    private companion object {
        private const val KEY_ANIMATION_PERCENTAGE_OF_TILES = "KEY_ANIMATION_PERCENTAGE_OF_TILES"
        private const val KEY_ANIMATION_STEP_DELAY = "KEY_ANIMATION_STEP_DELAY"
        private const val KEY_COUNT_OF_HORIZONTAL_TILES = "KEY_COUNT_OF_HORIZONTAL_TILES"
        private const val KEY_SUPER_STATE = "KEY_SUPER_STATE"
        private const val KEY_COUNT_OF_VERTICAL_TILES = "KEY_COUNT_OF_VERTICAL_TILES"
        private const val KEY_IS_ANIMATE = "KEY_IS_ANIMATE"
        private const val KEY_RGB_MODE_COLORS = "KEY_RGB_COLORS"
        private const val KEY_RGB_MODE_ENABLED = "KEY_RGB_MODE_ENABLED"
        private const val KEY_SOUND_ENABLED = "KEY_SOUND_ENABLED"
        private const val KEY_TILE_COLORS = "KEY_TILE_COLORS"
    }
}
