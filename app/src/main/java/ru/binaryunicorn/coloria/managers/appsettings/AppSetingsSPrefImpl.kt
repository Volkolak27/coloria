package ru.binaryunicorn.coloria.managers.appsettings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ru.binaryunicorn.coloria.enums.AnimationSpeed
import ru.binaryunicorn.coloria.enums.AnimationType

class AppSetingsSPrefImpl(context: Context) : IAppSettings
{
    private val _sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object
    {
        private const val HORIZONTAL_COUNT: String = "HORIZONTAL_COUNT"
        private const val DEFAULT_HORIZONTAL_COUNT: Int = 4

        private const val VERTICAL_COUNT: String = "VERTICAL_COUNT"
        private const val DEFAULT_VERTICAL_COUNT: Int = 4

        private const val TAP_SOUND_ENABLED: String = "TAP_SOUND_ENABLED"
        private const val DEFAULT_TAP_SOUND_ENABLED: Boolean = false

        private const val FULL_SCREEN_MODE: String = "FULL_SCREEN_MODE"
        private const val DEFAULT_FULL_SCREEN_MODE: Boolean = false

        private const val ANIMATION_ENABLED: String = "ANIMATION_ENABLED"
        private const val DEFAULT_ANIMATION_ENABLED: Boolean = false

        private const val ANIMATION_TYPE: String = "ANIMATION_TYPE"
        private val DEFAULT_ANIMATION_TYPE: AnimationType = AnimationType.HALF

        private const val ANIMATION_SPEED: String = "ANIMATION_SPEED"
        private val DEFAULT_ANIMATION_SPEED: AnimationSpeed = AnimationSpeed.MEDIUM
    }

    //// IAppSettings ////

    override fun putHorizontalCount(count: Int)
    {
        _sharedPreferences.edit().putInt(HORIZONTAL_COUNT, count).commit()
    }
    override fun obtainHorizontalCount(): Int = _sharedPreferences.getInt(HORIZONTAL_COUNT, DEFAULT_HORIZONTAL_COUNT)

    override fun putVerticalCount(count: Int)
    {
        _sharedPreferences.edit().putInt(VERTICAL_COUNT, count).commit()
    }
    override fun obtainVerticalCount(): Int = _sharedPreferences.getInt(VERTICAL_COUNT, DEFAULT_VERTICAL_COUNT)

    override fun putTapSoundEnabled(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(TAP_SOUND_ENABLED, enabled).commit()
    }
    override fun isTapSoundEnabled(): Boolean = _sharedPreferences.getBoolean(TAP_SOUND_ENABLED, DEFAULT_TAP_SOUND_ENABLED)

    override fun putFullScreenMode(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(FULL_SCREEN_MODE, enabled).commit()
    }
    override fun isFullScreenMode(): Boolean = _sharedPreferences.getBoolean(FULL_SCREEN_MODE, DEFAULT_FULL_SCREEN_MODE)

    override fun putAnimationEnabled(enabled: Boolean)
    {
        _sharedPreferences.edit().putBoolean(ANIMATION_ENABLED, enabled).commit()
    }
    override fun isAnimationEnabled(): Boolean = _sharedPreferences.getBoolean(ANIMATION_ENABLED, DEFAULT_ANIMATION_ENABLED)

    override fun putAnimationType(type: AnimationType)
    {
        _sharedPreferences.edit().putInt(ANIMATION_TYPE, type.id()).commit()
    }
    override fun obtainAnimationType(): AnimationType = AnimationType.fromId(_sharedPreferences.getInt(ANIMATION_TYPE, DEFAULT_ANIMATION_TYPE.id()))

    override fun putAnimationSpeed(speed: AnimationSpeed)
    {
        _sharedPreferences.edit().putInt(ANIMATION_SPEED, speed.id()).commit()
    }
    override fun obtainAnimationSpeed(): AnimationSpeed = AnimationSpeed.fromId(_sharedPreferences.getInt(ANIMATION_SPEED, DEFAULT_ANIMATION_SPEED.id()))
}