package ru.binaryunicorn.coloria.modules.tiletap.presenters

import android.os.Handler
import android.os.Message
import android.util.Log
import ru.binaryunicorn.coloria.AppConst
import ru.binaryunicorn.coloria.enums.AnimationSpeed
import ru.binaryunicorn.coloria.enums.AnimationType
import ru.binaryunicorn.coloria.managers.appsettings.IAppSettings
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapInput
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapOutput
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapPresenter
import ru.binaryunicorn.coloria.modules.tiletap.ITiletapView
import ru.binaryunicorn.coloria.pattern.BasePresenter
import java.util.*

class TiletapPresenter(view: ITiletapView, appSettings: IAppSettings, output: ITiletapOutput?) : BasePresenter<ITiletapView>(view), ITiletapPresenter, ITiletapInput
{
    private val _moduleOutput: ITiletapOutput? = output
    private val _appSettings: IAppSettings = appSettings

    private var _easyLaunchFlag: Boolean = false

    companion object
    {
        private const val ANIMATE_STEP = 1
    }

    private var _animationHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what)
            {
                ANIMATE_STEP -> _animateStep.run()
            }
        }
    }

    //// BasePresenter ////

    override fun viewIsReady()
    {
        super.viewIsReady()

        var horizontalSize = _appSettings.obtainHorizontalCount()
        var verticalSize = _appSettings.obtainVerticalCount()

        if (_easyLaunchFlag)
        {
            if (horizontalSize >= 4 || verticalSize >= 4)
            {
                horizontalSize = 4
                verticalSize = 4
            }

            _easyLaunchFlag = false
        }

        getView()?.updateFieldSize(horizontalSize, verticalSize)
        getView()?.updateTapSound(_appSettings.isTapSoundEnabled())
        getView()?.updateAnimation(_appSettings.isAnimationEnabled())

        fullscreenMode(_appSettings.isFullScreenMode())
        checkAnimationFunc()
    }

    override fun destroy()
    {
        _animationHandler.removeMessages(ANIMATE_STEP)
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
        fullscreenMode(enabled)
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

    override fun recreateTiletapSignal()
    {
        recreateTiletap()
    }

    override fun setupEasyLaunchFlag()
    {
        _easyLaunchFlag = true
    }

    override fun routeToSettings()
    {
        _moduleOutput?.routeToSettings()
    }

    //// ITiletapInput ////

    override fun sendFullscreenMode(enabled: Boolean)
    {
        getView()?.updateFullscreenMode(enabled)
    }

    override fun recreateTiletapField()
    {
        recreateTiletap()
    }

    //// Private ////

    private val _animateStep = Runnable {
        if (getView() == null) return@Runnable

        val horizontalSize = _appSettings.obtainHorizontalCount()
        val verticalSize = _appSettings.obtainVerticalCount()
        val animationType = _appSettings.obtainAnimationType()
        val animationSpeed = _appSettings.obtainAnimationSpeed()

        if (animationType == AnimationType.FULL)
        {
            for (y in 0 until verticalSize)
            {
                for (x in 0 until horizontalSize)
                {
                    getView()?.updateRandomColorForTile(x, y)
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
                getView()?.updateRandomColorForTile(horizontalRandomGen.nextInt(horizontalSize), verticalRandomGen.nextInt(verticalSize))
            }
        }

        getView()?.refreshTiletapField()

        if (_appSettings.isAnimationEnabled())
        {
            _animationHandler.sendEmptyMessageDelayed(
                ANIMATE_STEP,
                when (animationSpeed)
                {
                    AnimationSpeed.SLOW -> 2000
                    AnimationSpeed.MEDIUM -> 1200
                    AnimationSpeed.FAST -> 600
                    AnimationSpeed.VERYFAST -> 200
                }
            )
        }
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
            Log.e(AppConst.LOGTAG, "Размер поля: $_appSettings.obtainHorizontalCount() на $_appSettings.obtainVerticalCount() - вызвал ошибку OutOfMemoryError")
            _appSettings.putHorizontalCount(4)
            _appSettings.putVerticalCount(4)
            _moduleOutput?.showFieldSizeError()
        }

        checkAnimationFunc()
    }

    private fun fullscreenMode(enabled: Boolean)
    {
        getView()?.updateFullscreenMode(enabled)

        if (enabled)
        {
            _moduleOutput?.requestFullscreenMode()
        }
    }

    private fun checkAnimationFunc()
    {
        _animationHandler.removeMessages(ANIMATE_STEP)

        if (_appSettings.isAnimationEnabled())
        {
            _animateStep.run()
        }
    }
}