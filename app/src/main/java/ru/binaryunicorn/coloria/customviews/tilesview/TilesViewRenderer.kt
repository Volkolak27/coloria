package ru.binaryunicorn.coloria.customviews.tilesview

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import ru.binaryunicorn.coloria.Consts
import ru.binaryunicorn.coloria.common.structs.colors.FloatRgbColor
import ru.binaryunicorn.coloria.common.utils.OpenGlUtils

class TilesViewRenderer : GLSurfaceView.Renderer {

    private var tilesOnWidth = 0
    private var tilesOnHeight = 0
    private var buffer: FloatBuffer? = null
    private var needToAttachBuffer = true
    private var shaderProgramId = 0

    //// Public ////

    fun configure(colors: Array<Array<FloatRgbColor>>, tilesOnWidth: Int, tilesOnHeight: Int) {
        this.tilesOnWidth = tilesOnWidth
        this.tilesOnHeight = tilesOnHeight

        val tileWidth = MATRIX_SIZE / tilesOnWidth
        val tileHeight = MATRIX_SIZE / tilesOnHeight
        val arraySize = tilesOnWidth * tilesOnHeight * (VERTEX_IN_QUAD * VERTEX_COMPONENTS)
        val vertexes = FloatArray(arraySize)

        var offset = 0
        for (y in 0 until tilesOnHeight) {
            for (x in 0 until tilesOnWidth) {
                val baseX = tileWidth * x
                val baseY = tileHeight * y
                val floatColor = colors[y][x]

                // Первый полигон
                // 1 - левый низ
                vertexes[offset]   = baseX
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 2 - левый верх
                vertexes[offset]   = baseX
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 3 - правый верх
                vertexes[offset]   = baseX + tileWidth
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // Второй полигон
                // 4 - левый низ
                vertexes[offset]   = baseX
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 5 - правый верх
                vertexes[offset]   = baseX + tileWidth
                vertexes[offset+1] = baseY + tileHeight
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS

                // 6 - правый низ
                vertexes[offset]   = baseX + tileWidth
                vertexes[offset+1] = baseY
                vertexes[offset+2] = floatColor.red
                vertexes[offset+3] = floatColor.green
                vertexes[offset+4] = floatColor.blue
                offset += VERTEX_COMPONENTS
            }
        }

        buffer =
            ByteBuffer
                .allocateDirect(arraySize * Consts.FLOAT_SIZEOF)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .also {
                    it.clear()
                    it.put(vertexes)
                }

        needToAttachBuffer = true
    }

    fun updateTile(x: Int, y: Int, toColor: FloatRgbColor) {
        // приведение начала координат в систему OpenGL
        val glX = x
        val glY = (tilesOnHeight - 1) - y

        val offset = (glY * tilesOnWidth + glX) * (VERTEX_COMPONENTS * VERTEX_IN_QUAD)

        if (buffer == null) return
        with(buffer!!) {
            put(offset + 2, toColor.red)
            put(offset + 3, toColor.green)
            put(offset + 4, toColor.blue)

            put(offset + 7, toColor.red)
            put(offset + 8, toColor.green)
            put(offset + 9, toColor.blue)

            put(offset + 12, toColor.red)
            put(offset + 13, toColor.green)
            put(offset + 14, toColor.blue)

            put(offset + 17, toColor.red)
            put(offset + 18, toColor.green)
            put(offset + 19, toColor.blue)

            put(offset + 22, toColor.red)
            put(offset + 23, toColor.green)
            put(offset + 24, toColor.blue)

            put(offset + 27, toColor.red)
            put(offset + 28, toColor.green)
            put(offset + 29, toColor.blue)
        }
    }

    //// GLSurfaceView.Renderer ////

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        val vertexShaderId = OpenGlUtils.createShader(GLES20.GL_VERTEX_SHADER, SHADER_VERTEX)
        val fragmentShaderId = OpenGlUtils.createShader(GLES20.GL_FRAGMENT_SHADER, SHADER_FRAGMENT)
        shaderProgramId = OpenGlUtils.createShaderProgram(vertexShaderId, fragmentShaderId)

        if (vertexShaderId != 0 && fragmentShaderId != 0 && shaderProgramId != 0) {
            GLES20.glUseProgram(shaderProgramId)

            val projMatrix =
                OpenGlUtils.makeProjectionOrthoMatrix(
                    left = 0.0f, right = MATRIX_SIZE,
                    bottom = 0.0f, top = MATRIX_SIZE,
                    near = 1.0f, far = MATRIX_SIZE
                )
            val viewMatrix =
                OpenGlUtils.makeViewMatrix(
                    eyeX = 0.0f, eyeY = 0.0f, eyeZ = MATRIX_SIZE / 2,
                    centerX = 0.0f, centerY = 0.0f, centerZ = 0.0f,
                    upX = 0.0f, upY = 1.0f, upZ = 0.0f
                )

            val matrix = FloatArray(16)
            Matrix.multiplyMM(matrix, 0, projMatrix, 0, viewMatrix, 0)

            val uMatrixLocation = GLES20.glGetUniformLocation(shaderProgramId, "u_Matrix")
            GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

            reAttachBufferIfNeeded()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        reAttachBufferIfNeeded()

        if (buffer != null) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, tilesOnWidth * tilesOnHeight * VERTEX_IN_QUAD)
        }
    }

    //// Private ////

    private fun reAttachBufferIfNeeded() {
        if (needToAttachBuffer && buffer != null) {
            val aPositionLocation = GLES20.glGetAttribLocation(shaderProgramId, "a_Position")
            buffer!!.position(0)
            GLES20.glVertexAttribPointer(
                aPositionLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                VERTEX_COMPONENTS * Consts.FLOAT_SIZEOF,
                buffer!!
            )
            GLES20.glEnableVertexAttribArray(aPositionLocation)

            val aColorLocation = GLES20.glGetAttribLocation(shaderProgramId, "a_Color")
            buffer!!.position(2)
            GLES20.glVertexAttribPointer(
                aColorLocation,
                3,
                GLES20.GL_FLOAT,
                false,
                VERTEX_COMPONENTS * Consts.FLOAT_SIZEOF,
                buffer!!
            )
            GLES20.glEnableVertexAttribArray(aColorLocation)

            needToAttachBuffer = false
        }
    }

    private companion object {
        private const val VERTEX_COMPONENTS = 5
        private const val VERTEX_IN_QUAD = 6
        private const val MATRIX_SIZE = 1000.0f

        private val SHADER_VERTEX =
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

        private val SHADER_FRAGMENT =
        """
            precision mediump float;
    
            varying vec4 v_Color;
    
            void main()
            {
                gl_FragColor = v_Color;
            }
        """.trimIndent()
    }
}
