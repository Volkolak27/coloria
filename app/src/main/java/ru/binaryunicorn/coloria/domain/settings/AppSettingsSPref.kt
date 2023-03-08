package ru.binaryunicorn.coloria.domain.settings

import android.content.Context
import androidx.preference.PreferenceManager
import ru.binaryunicorn.coloria.Consts
import ru.binaryunicorn.coloria.common.enums.AnimationSpeed
import ru.binaryunicorn.coloria.common.enums.AnimationType

class AppSettingsSPref(context: Context) : IAppSettings {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    //// IAppSettings ////

    override fun putHorizontalTilesCount(count: Int) {
        sharedPreferences.edit().putInt(HORIZONTAL_TILES_COUNT, count).apply()
    }

    override fun obtainHorizontalTilesCount(): Int =
        sharedPreferences.getInt(HORIZONTAL_TILES_COUNT, DEFAULT_HORIZONTAL_TILES_COUNT)

    override fun putVerticalTilesCount(count: Int) {
        sharedPreferences.edit().putInt(VERTICAL_TILES_COUNT, count).apply()
    }

    override fun obtainVerticalTilesCount(): Int =
        sharedPreferences.getInt(VERTICAL_TILES_COUNT, DEFAULT_VERTICAL_TILES_COUNT)

    override fun putTapSoundEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(TAP_SOUND_ENABLED, enabled).apply()
    }

    override fun isTapSoundEnabled(): Boolean =
        sharedPreferences.getBoolean(TAP_SOUND_ENABLED, DEFAULT_TAP_SOUND_ENABLED)

    override fun putFullScreenEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(FULL_SCREEN_ENABLED, enabled).apply()
    }

    override fun isFullScreenEnabled(): Boolean =
        sharedPreferences.getBoolean(FULL_SCREEN_ENABLED, DEFAULT_FULL_SCREEN_ENABLED)

    override fun putAnimationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(ANIMATION_ENABLED, enabled).apply()
    }

    override fun isAnimationEnabled(): Boolean =
        sharedPreferences.getBoolean(ANIMATION_ENABLED, DEFAULT_ANIMATION_ENABLED)

    override fun putAnimationType(type: AnimationType) {
        sharedPreferences.edit().putInt(ANIMATION_TYPE, type.ordinal).apply()
    }

    override fun obtainAnimationType(): AnimationType =
        AnimationType.fromOrdinal(sharedPreferences.getInt(ANIMATION_TYPE, DEFAULT_ANIMATION_TYPE.ordinal))

    override fun putAnimationSpeed(speed: AnimationSpeed) {
        sharedPreferences.edit().putInt(ANIMATION_SPEED, speed.ordinal).apply()
    }

    override fun obtainAnimationSpeed(): AnimationSpeed =
        AnimationSpeed.fromOrdinal(sharedPreferences.getInt(ANIMATION_SPEED, DEFAULT_ANIMATION_SPEED.ordinal))

    companion object {
        private const val HORIZONTAL_TILES_COUNT = "HORIZONTAL_TILES_COUNT"
        private const val DEFAULT_HORIZONTAL_TILES_COUNT = Consts.DEFAULT_HORIZONTAL_TILES_COUNT

        private const val VERTICAL_TILES_COUNT = "VERTICAL_TILES_COUNT"
        private const val DEFAULT_VERTICAL_TILES_COUNT = Consts.DEFAULT_VERTICAL_TILES_COUNT

        private const val TAP_SOUND_ENABLED = "TAP_SOUND_ENABLED"
        private const val DEFAULT_TAP_SOUND_ENABLED = Consts.DEFAULT_TAP_SOUND_ENABLED

        private const val FULL_SCREEN_ENABLED = "FULL_SCREEN_ENABLED"
        private const val DEFAULT_FULL_SCREEN_ENABLED = Consts.DEFAULT_FULL_SCREEN_ENABLED

        private const val ANIMATION_ENABLED = "ANIMATION_ENABLED"
        private const val DEFAULT_ANIMATION_ENABLED = Consts.DEFAULT_ANIMATION_ENABLED

        private const val ANIMATION_TYPE = "ANIMATION_TYPE"
        private val DEFAULT_ANIMATION_TYPE = Consts.DEFAULT_ANIMATION_TYPE

        private const val ANIMATION_SPEED = "ANIMATION_SPEED"
        private val DEFAULT_ANIMATION_SPEED = Consts.DEFAULT_ANIMATION_SPEED
    }
}
