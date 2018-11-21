package com.example.djd.fingertest;

import android.opengl.GLES20;

/**
 * Created by djd on 18-11-19.
 */

public class Util {

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
