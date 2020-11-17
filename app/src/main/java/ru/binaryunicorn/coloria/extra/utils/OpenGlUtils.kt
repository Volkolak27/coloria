package ru.binaryunicorn.coloria.extra.utils

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import ru.binaryunicorn.coloria.App

class OpenGlUtils
{
    companion object
    {
        fun createShader(shaderType: Int, sourceCode: String): Int
        {
            val shaderId = GLES20.glCreateShader(shaderType)
            if (shaderId == GLES20.GL_FALSE)
            {
                Log.e(App.Consts.LOGTAG, "Шейдер не создан (на этапе выбора типа)")
                return 0
            }

            GLES20.glShaderSource(shaderId, sourceCode)
            GLES20.glCompileShader(shaderId)

            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == GLES20.GL_FALSE)
            {
                GLES20.glDeleteShader(shaderId)
                Log.e(App.Consts.LOGTAG, "Ошибка при компиляции шейдера")
                return 0
            }

            return shaderId
        }

        fun createProgram(vertexShaderId: Int, fragmentShaderId: Int): Int
        {
            val programId = GLES20.glCreateProgram()
            if (programId == GLES20.GL_FALSE)
            {
                Log.e(App.Consts.LOGTAG, "Ошибка при создании шейдерной программы")
                return 0
            }

            GLES20.glAttachShader(programId, vertexShaderId)
            GLES20.glAttachShader(programId, fragmentShaderId)
            GLES20.glLinkProgram(programId)

            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == GLES20.GL_FALSE)
            {
                Log.e(App.Consts.LOGTAG, "Ошибка при присоединении шейдеров к шейдерной программе")
                GLES20.glDeleteProgram(programId)
                return 0
            }

            return programId
        }

        fun makeProjectionOrthoMatrix(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): FloatArray
        {
            val matrix = FloatArray(16)
            Matrix.orthoM(matrix, 0, left, right, bottom, top, near, far)
            return matrix
        }

        fun makeProjectionFrustrimMatrix(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): FloatArray
        {
            val matrix = FloatArray(16)
            Matrix.frustumM(matrix, 0, left, right, bottom, top, near, far)
            return matrix
        }

        fun makeViewMatrix(eyeX: Float, eyeY: Float, eyeZ: Float, centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float, upZ: Float): FloatArray
        {
            val matrix = FloatArray(16)
            Matrix.setLookAtM(matrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
            return matrix
        }
    }
}