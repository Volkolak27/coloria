package ru.binaryunicorn.coloria.modules.tiletap.presenters

import android.util.Log
import kotlinx.coroutines.*
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.extra.enums.AnimationSpeed
import ru.binaryunicorn.coloria.extra.enums.AnimationType
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapPresenter
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapView
import ru.binaryunicorn.coloria.pattern.BasePresenter
import java.util.*

class TiletapPresenter constructor(appSettings: IAppSettings) : BasePresenter<ITiletapView>(), ITiletapPresenter
{
    private val _appSettings: IAppSettings = appSettings

    private var _animationJob: Job? = null

    //// BasePresenter ////

    override fun viewIsReady()
    {
        super.viewIsReady()

        getView()?.updateFieldSize(_appSettings.obtainHorizontalCount(), _appSettings.obtainVerticalCount())
        getView()?.updateTapSound(_appSettings.isTapSoundEnabled())
        getView()?.updateAnimation(_appSettings.isAnimationEnabled())
        getView()?.updateFullscreenMode(_appSettings.isFullScreenMode())

        checkAnimationFunc()
    }

    override fun destroy()
    {
        _animationJob?.cancel()
    }

    //// ITiletapPresenter ////

    override fun fieldSizeChanged(horizontalCount: Int, verticalCount: Int)
    {
        _appSettings.putHorizontalCount(horizontalCount)
        _appSettings.putVerticalCount(verticalCount)
        getView()?.updateFieldSize(horizontalCount, verticalCount)
    }

    override fun fullscreenModeChanged(enabled: Boolean)
    {
        _appSettings.putFullScreenMode(enabled)
        getView()?.updateFullscreenMode(enabled)
    }

    override fun rgbTestAction()
    {
        getView()?.openRgbTest()
    }

    override fun tapSoundChanged(enabled: Boolean)
    {
        _appSettings.putTapSoundEnabled(enabled)
        getView()?.updateTapSound(enabled)
    }

    override fun animationChanged(enabled: Boolean)
    {
        _appSettings.putAnimationEnabled(enabled)
        getView()?.updateAnimation(enabled)
        checkAnimationFunc()
    }

    override fun refreshTiletapFieldAction()
    {
        recreateTiletap()
    }

    override fun resetTiletapField()
    {
        val horizontalSize = App.Consts.DEFAULT_TILES_COUNT_ON_HORIZONTAL
        val verticalSize = App.Consts.DEFAULT_TILES_COUNT_ON_VERTICAL

        _appSettings.putHorizontalCount(horizontalSize)
        _appSettings.putVerticalCount(verticalSize)
        getView()?.updateFieldSize(horizontalSize, verticalSize)
    }

    override fun toSettingsAction()
    {
        getView()?.openSettings()
    }

    //// Private ////

    private fun animateStep()
    {
        if (getView() == null) return

        val horizontalSize = _appSettings.obtainHorizontalCount()
        val verticalSize = _appSettings.obtainVerticalCount()
        val animationType = _appSettings.obtainAnimationType()

        if (animationType == AnimationType.FULL)
        {
            for (y in 0 until verticalSize)
            {
                for (x in 0 until horizontalSize)
                {
                    getView()?.updateTileWithRandomColor(x, y)
                }
            }
        }
        else
        {
            val fieldSize = verticalSize * horizontalSize
            val tilesToChange = when (animationType)
                                {
                                    AnimationType.LOW  -> fieldSize * 0.25f
                                    AnimationType.HALF -> fieldSize * 0.50f
                                    AnimationType.BIG  -> fieldSize * 0.75f
                                    AnimationType.FULL -> fieldSize * 1.00f
                                }

            val horizontalRandomGen = Random()
            val verticalRandomGen = Random()
            for (i in 0 until tilesToChange.toInt())
            {
                // одна и таже плитка может окрашиваться несколько раз, это хорошо - обеспечиваем "случайность" количества действа
                getView()?.updateTileWithRandomColor(horizontalRandomGen.nextInt(horizontalSize), verticalRandomGen.nextInt(verticalSize))
            }
        }

        getView()?.refreshTiletapField()
    }

    private fun recreateTiletap()
    {
        try
        {
            getView()?.updateFieldSize(_appSettings.obtainHorizontalCount(), _appSettings.obtainVerticalCount())
            getView()?.updateTapSound(_appSettings.isTapSoundEnabled())
            getView()?.updateAnimation(_appSettings.isAnimationEnabled())
        }
        catch (e: OutOfMemoryError)
        {
            Log.e(App.Consts.LOGTAG, "Размер поля: ${_appSettings.obtainHorizontalCount()} на ${_appSettings.obtainVerticalCount()} - вызвал ошибку OutOfMemoryError")
            _appSettings.putHorizontalCount(App.Consts.DEFAULT_TILES_COUNT_ON_HORIZONTAL)
            _appSettings.putVerticalCount(App.Consts.DEFAULT_TILES_COUNT_ON_VERTICAL)
            getView()?.showFieldSizeError()
        }

        checkAnimationFunc()
    }

    private fun checkAnimationFunc()
    {
        if (_appSettings.isAnimationEnabled())
        {
            GlobalScope.launch {
                while (isActive && _appSettings.isAnimationEnabled())
                {
                    withContext(Dispatchers.Main) {
                        animateStep()
                    }
                    delay(
                        when (_appSettings.obtainAnimationSpeed())
                        {
                            AnimationSpeed.SLOW -> 2000L
                            AnimationSpeed.MEDIUM -> 1200L
                            AnimationSpeed.FAST -> 600L
                            AnimationSpeed.VERYFAST -> 200L
                        }
                    )
                }
            }
        }
        else
        {
            _animationJob?.cancel()
        }
    }
}