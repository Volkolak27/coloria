package ru.binaryunicorn.coloria.extra.customviews.tilesview

import android.content.Context
import android.graphics.Point
import android.media.SoundPool
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.binaryunicorn.coloria.extra.structures.RgbColor
import java.util.*
import kotlin.math.ceil

class TilesView(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs), View.OnTouchListener
{
    private val KEY_COUNT_OF_HORIZONTAL_TILES = "KEY_COUNT_OF_HORIZONTAL_TILES"
    private val KEY_COUNT_OF_VERTICAL_TILES = "KEY_COUNT_OF_VERTICAL_TILES"
    private val KEY_SOUND_ENABLED = "KEY_SOUND_ENABLED"
    private val KEY_TILE_COLORS = "KEY_TILE_COLORS"
    private val KEY_RGB_TEST_ENABLED = "KEY_RGB_TEST_ENABLED"

    private val _renderer = TilesViewRenderer()
    private var _colors: Array<Array<RgbColor>> = arrayOf()
    private var _tilesOnWidth = 0
    private var _tilesOnHeight = 0
    private val _tapInfos = Array(10) { Point(-1, -1) }

    private var _rgbTest = false
    private var _soundEnabled = false
    private var _soundPool: SoundPool? = null
    private var _sounds = intArrayOf()

    private val _colorRandom = Random()
    private val _rgbTestColors: Queue<RgbColor> = LinkedList( listOf(RgbColor(255, 0, 0), RgbColor(0, 255, 0), RgbColor(0, 0, 255), RgbColor(255, 255, 255)) )
    private val _soundRandom = Random()

    init
    {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        setRenderer(_renderer)

        setOnTouchListener(this)
    }

    //// View ////

    override fun onSaveInstanceState(): Parcelable?
    {
        val bundle = Bundle()

        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt(KEY_COUNT_OF_HORIZONTAL_TILES, _tilesOnWidth)
        bundle.putInt(KEY_COUNT_OF_VERTICAL_TILES, _tilesOnHeight)
        bundle.putBoolean(KEY_SOUND_ENABLED, _soundEnabled)
        bundle.putBoolean(KEY_RGB_TEST_ENABLED, _rgbTest)
        bundle.putString(KEY_TILE_COLORS, Gson().toJson(_colors))

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?)
    {
        if (state is Bundle)
        {
            _tilesOnWidth = state.getInt(KEY_COUNT_OF_HORIZONTAL_TILES)
            _tilesOnHeight = state.getInt(KEY_COUNT_OF_VERTICAL_TILES)
            _soundEnabled = state.getBoolean(KEY_SOUND_ENABLED)
            _rgbTest = state.getBoolean(KEY_RGB_TEST_ENABLED)
            _colors = Gson().fromJson(state.getString(KEY_TILE_COLORS), object : TypeToken<Array<Array<RgbColor>>>() {}.type)

            _renderer.configure(_colors, _tilesOnWidth, _tilesOnHeight)

            super.onRestoreInstanceState(state.getParcelable("superState"))
        }
    }

    //// Public ////

    fun refresh()
	{
		invalidate()
	}

    fun generateField(tilesOnWidth: Int, tilesOnHeight: Int)
    {
        // Генерация случайных цветов
        _colors = Array(tilesOnHeight) { Array(tilesOnWidth) {
            RgbColor(
                _colorRandom.nextInt(255),
                _colorRandom.nextInt(255),
                _colorRandom.nextInt(255)
            )
        } }

        _tilesOnWidth = tilesOnWidth
        _tilesOnHeight = tilesOnHeight
        _renderer.configure(_colors, tilesOnWidth, tilesOnHeight)
    }

    fun setRandomColorForTile(horizontalPosition: Int, verticalPosition: Int)
    {
        try
        {
            _colors[verticalPosition][horizontalPosition] =
                if (_rgbTest)
                {
                    val nextColor = _rgbTestColors.remove()
                    _rgbTestColors.add(nextColor)
                    nextColor
                }
                else
                {
                    RgbColor(
                        _colorRandom.nextInt(255), _colorRandom.nextInt(255), _colorRandom.nextInt(255)
                    )
                }

            _renderer.updateTile(_colors[verticalPosition][horizontalPosition], horizontalPosition, verticalPosition)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun soundEnabled(enabled: Boolean)
    {
        _soundEnabled = enabled
    }

    fun updateSounds(soundPool: SoundPool, sounds: IntArray)
    {
        _soundPool = soundPool
        _sounds = sounds
    }

    fun releaseSounds()
    {
        _soundPool?.release()
        _soundPool = null
        _sounds = intArrayOf()
    }

    fun isRgbTest(): Boolean
    {
        return _rgbTest
    }

    fun enableRgbTest()
    {
        generateField(1, 1)
        _rgbTest = true
        setRandomColorForTile(0, 0)
    }

    fun disableRgbTest()
    {
        _rgbTest = false
    }

    //// View.OnTouchListener ////

    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        val tileWidth = ceil( width.toDouble() / _tilesOnWidth.toDouble() ).toInt()
        val tileHeight = ceil( height.toDouble() / _tilesOnHeight.toDouble() ).toInt()

        // косание за границей вью
        if ( event != null && (event.x < 0 || event.x > width || event.y < 0 || event.y > height) )
        {
            return true
        }
        val touchEvent = event!!

        when (touchEvent.actionMasked)
        {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE ->
            {
                for (i in 0 until touchEvent.pointerCount)
                {
                    val id = event.getPointerId(i)
                    val horizontalPosition = touchEvent.getX(i).toInt() / tileWidth
                    val verticalPosition = touchEvent.getY(i).toInt() / tileHeight

                    if (horizontalPosition != _tapInfos[id].x || verticalPosition != _tapInfos[id].y)
                    {
                        _tapInfos[id].x = horizontalPosition
                        _tapInfos[id].y = verticalPosition

                        setRandomColorForTile(horizontalPosition, verticalPosition)

                        if (_soundEnabled && _soundPool != null && _sounds.isNotEmpty())
                        {
                            _soundPool?.play( _sounds[_soundRandom.nextInt(_sounds.size)], 1.0f, 1.0f, 0, 0, 1.0f )
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP ->
            {
                for (item in _tapInfos)
                {
                    item.x = -1
                    item.y = -1
                }
            }
            MotionEvent.ACTION_POINTER_UP ->
            {
                val id = event.getPointerId(event.actionIndex)
                _tapInfos[id].x = -1
                _tapInfos[id].y = -1
            }
        }

        refresh()
        return true
    }
}