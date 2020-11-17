package ru.binaryunicorn.coloria.managers.appsettings

import android.content.Context
import androidx.preference.PreferenceManager
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.extra.enums.AnimationSpeed
import ru.binaryunicorn.coloria.extra.enums.AnimationType

class AppSetingsSPref(context: Context) : IAppSettings
{
    private val _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object
    {
        private const val HORIZONTAL_COUNT = "HORIZONTAL_COUNT"
        private const val DEFAULT_HORIZONTAL_COUNT = App.Consts.DEFAULT_TILES_COUNT_ON_HORIZONTAL

        private const val VERTICAL_COUNT = "VERTICAL_COUNT"
        private const val DEFAULT_VERTICAL_COUNT = App.Consts.DEFAULT_TILES_COUNT_ON_VERTICAL

        private const val TAP_SOUND_ENABLED = "TAP_SOUND_ENABLED"
        private const val DEFAULT_TAP_SOUND_ENABLED = true

        private const val FULL_SCREEN_MODE = "FULL_SCREEN_MODE"
        private const val DEFAULT_FULL_SCREEN_MODE = false

        private const val ANIMATION_ENABLED = "ANIMATION_ENABLED"
        private const val DEFAULT_ANIMATION_ENABLED = false

        private const val ANIMATION_TYPE = "ANIMATION_TYPE"
        private val DEFAULT_ANIMATION_TYPE = AnimationType.HALF

        private const val ANIMATION_SPEED = "ANIMATION_SPEED"
        private val DEFAULT_ANIMATION_SPEED = AnimationSpeed.MEDIUM
    }

    //// IAppSettings ////

    override fun putHorizontalCount(count: Int)
    {
        _sharedPreferences.edit().putInt(HORIZONTAL_COUNT, count).apply()
    }
    override fun obtainHorizontalCount(): Int = _sharedPreferences.getInt(HORIZONTAL_COUNT, DEFAULT_HORIZONTAL_COUNT)

    override fun putVerticalCount(count: Int)
    {
        _sharedPreferences.edit().putInt(VERTICAL_COUNT, count).apply()
    }
    override fun obtainVerticalCount(): Int = _sharedPreferences.getInt(VERTICAL_COUNT, DEFAULT_VERTICAL_COUNT)

    override fun putTapSoundEnabled(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(TAP_SOUND_ENABLED, enabled).apply()
    }
    override fun isTapSoundEnabled(): Boolean = _sharedPreferences.getBoolean(TAP_SOUND_ENABLED, DEFAULT_TAP_SOUND_ENABLED)

    override fun putFullScreenMode(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(FULL_SCREEN_MODE, enabled).apply()
    }
    override fun isFullScreenMode(): Boolean = _sharedPreferences.getBoolean(FULL_SCREEN_MODE, DEFAULT_FULL_SCREEN_MODE)

    override fun putAnimationEnabled(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(ANIMATION_ENABLED, enabled).apply()
    }
    override fun isAnimationEnabled(): Boolean = _sharedPreferences.getBoolean(ANIMATION_ENABLED, DEFAULT_ANIMATION_ENABLED)

    override fun putAnimationType(type: AnimationType)
    {
        _sharedPreferences.edit().putInt(ANIMATION_TYPE, type.id()).apply()
    }
    override fun obtainAnimationType(): AnimationType = AnimationType.fromId(_sharedPreferences.getInt(ANIMATION_TYPE, DEFAULT_ANIMATION_TYPE.id()))

    override fun putAnimationSpeed(speed: AnimationSpeed)
    {
        _sharedPreferences.edit().putInt(ANIMATION_SPEED, speed.id()).apply()
    }
    override fun obtainAnimationSpeed(): AnimationSpeed = AnimationSpeed.fromId(_sharedPreferences.getInt(ANIMATION_SPEED, DEFAULT_ANIMATION_SPEED.id()))
}