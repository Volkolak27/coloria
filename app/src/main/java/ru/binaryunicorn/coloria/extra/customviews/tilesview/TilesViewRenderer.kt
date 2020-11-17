package ru.binaryunicorn.coloria.extra.customviews.tilesview

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import ru.binaryunicorn.coloria.App
import ru.binaryunicorn.coloria.extra.structures.RgbColor
import ru.binaryunicorn.coloria.extra.utils.OpenGlUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TilesViewRenderer : GLSurfaceView.Renderer
{
    private val VERTEX_COMPONENTS = 5
    private val VERTEX_IN_QUAD = 6
    private val MATRIX_SIZE = 1000.0f

    private var _tilesOnWidth = 0
    private var _tilesOnHeight = 0
    private var _shaderProgramId = 0
    private var _buffer: FloatBuffer? = null
    private var _needToAttachBuffer = true

    private val _vertexShaderCode =
    """
        uniform mat4 u_Matrix;
        attribute vec4 a_Position;
        attribute vec4 a_Color;

        varying vec4 v_Color;

        void main()
        {
            v_Color = a_Color;
            gl_Position = u_Matrix * a_Position;
        }
    """.trimIndent()

    private val _fragmentShaderCode =
    """
        precision mediump float;

        varying vec4 v_Color;

        void main()
        {
            gl_FragColor = v_Color;
        }
    """.trimIndent()

    //// GLSurfaceView.Renderer ////

    override fun onDrawFrame(gl: GL10?)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        reAttachBufferIfNeeded()

        if (_buffer != null)
        {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _tilesOnWidth * _tilesOnHeight * VERTEX_IN_QUAD)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)
    {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)
    {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        val vertexShaderId = OpenGlUtils.createShader(GLES20.GL_VERTEX_SHADER, _vertexShaderCode)
        val fragmentShaderId = OpenGlUtils.createShader(GLES20.GL_FRAGMENT_SHADER, _fragmentShaderCode)
        _shaderProgramId = OpenGlUtils.createProgram(vertexShaderId, fragmentShaderId)

        if (vertexShaderId != 0 || fragmentShaderId != 0 || _shaderProgramId != 0)
        {
            GLES20.glUseProgram(_shaderProgramId)

            val matrix = FloatArray(16)
            val projMatrix = OpenGlUtils.makeProjectionOrthoMatrix(left = 0.0f, right = MATRIX_SIZE, bottom = 0.0f, top = MATRIX_SIZE, near = 1.0f, far = 100.0f)
            val viewMatrix = OpenGlUtils.makeViewMatrix(eyeX = 0.0f, eyeY = 0.0f, eyeZ = 10.0f, centerX = 0.0f, centerY = 0.0f, centerZ = 0.0f, upX = 0.0f, upY = 1.0f, upZ = 0.0f)

            val uMatrixLocation = GLES20.glGetUniformLocation(_shaderProgramId, "u_Matrix")
            Matrix.multiplyMM(matrix, 0, projMatrix, 0, viewMatrix, 0)
            GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

            reAttachBufferIfNeeded()
        }
    }

    //// Public ////

    fun configure(colors: Array<Array<RgbColor>>, tilesOnWidth: Int, tilesOnHeight: Int)
    {
        _tilesOnWidth = tilesOnWidth
        _tilesOnHeight = tilesOnHeight

        val tileWidth = MATRIX_SIZE / tilesOnWidth
        val tileHeight = MATRIX_SIZE / tilesOnHeight
        val arraySize = tilesOnWidth * tilesOnHeight * (VERTEX_IN_QUAD * VERTEX_COMPONENTS)
        val vertexes = FloatArray(arraySize)

        var offset = 0
        for (y in 0 until tilesOnHeight)
        {
            for (x in 0 until tilesOnWidth)
            {
                val baseX = tileWidth * x
                val baseY = tileHeight * y
                val floatColor = colors[y][x].toFloats()

                // 1 - левый низ
                vertexes[offset] = baseX
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 2 - левый верх
                vertexes[offset] = baseX
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 3 - правый верх
                vertexes[offset] = baseX + tileWidth
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 4 - левый низ
                vertexes[offset] = baseX
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 5 - правый верх
                vertexes[offset] = baseX + tileWidth
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 6 - правый низ
                vertexes[offset] = baseX + tileWidth
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS
            }
        }

        _buffer = ByteBuffer.allocateDirect(arraySize * App.Consts.FLOAT_SIZEOF).order(ByteOrder.nativeOrder()).asFloatBuffer().let {
            it.clear()
            it.put(vertexes)
        }
        _needToAttachBuffer = true
    }

    fun updateTile(color: RgbColor, x: Int, y: Int)
    {
        if (_buffer == null) return

        // приведение начала координат в систему OpenGL
        val openglX = x
        val openglY = (_tilesOnHeight-1) - y

        val offset = (openglY * _tilesOnWidth + openglX) * (VERTEX_COMPONENTS * VERTEX_IN_QUAD)
        val floatColor = color.toFloats()

        with(_buffer!!)
        {
            put(offset + 2, floatColor.red)
            put(offset + 3, floatColor.green)
            put(offset + 4, floatColor.blue)

            put(offset + 7, floatColor.red)
            put(offset + 8, floatColor.green)
            put(offset + 9, floatColor.blue)

            put(offset + 12, floatColor.red)
            put(offset + 13, floatColor.green)
            put(offset + 14, floatColor.blue)

            put(offset + 17, floatColor.red)
            put(offset + 18, floatColor.green)
            put(offset + 19, floatColor.blue)

            put(offset + 22, floatColor.red)
            put(offset + 23, floatColor.green)
            put(offset + 24, floatColor.blue)

            put(offset + 27, floatColor.red)
            put(offset + 28, floatColor.green)
            put(offset + 29, floatColor.blue)
        }
    }

    //// Private ////

    private fun reAttachBufferIfNeeded()
    {
        if (_needToAttachBuffer && _buffer != null)
        {
            val aPositionLocation = GLES20.glGetAttribLocation(_shaderProgramId, "a_Position")
            _buffer!!.position(0)
            GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, VERTEX_COMPONENTS * App.Consts.FLOAT_SIZEOF, _buffer!!)
            GLES20.glEnableVertexAttribArray(aPositionLocation)

            val aColorLocation = GLES20.glGetAttribLocation(_shaderProgramId, "a_Color")
            _buffer!!.position(2)
            GLES20.glVertexAttribPointer(aColorLocation, 3, GLES20.GL_FLOAT, false, VERTEX_COMPONENTS * App.Consts.FLOAT_SIZEOF, _buffer!!)
            GLES20.glEnableVertexAttribArray(aColorLocation)

            _needToAttachBuffer = false
        }
    }
}