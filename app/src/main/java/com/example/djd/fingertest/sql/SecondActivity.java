package com.example.djd.fingertest.sql;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.djd.fingertest.R;
import com.example.djd.fingertest.Triangle;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by djd on 18-11-16.
 */

public class SecondActivity extends Activity implements GLSurfaceView.Renderer {
    SQLiteDatabase db;
    private static final String TAG = "SecondActivity";
    private GLSurfaceView mGLV;
    private Triangle triangle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);
        MyDatabaseHepler myDatabaseHepler = new MyDatabaseHepler(this, "demo.db", null, 1);
       // db = myDatabaseHepler.getWritableDatabase("123123");
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mGLV = findViewById(R.id.glv_test);

        mGLV.setRenderer(this);
        //myDatabaseHepler = new MyDatabaseHepler(this,"demo.db", null, 1);
    }


    private void addBook(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "hello word");
        contentValues.put("page",25);
        db.insert("book,", null, contentValues);

    }

    private void queryBook(){
        Cursor cursor = db.query("book",null,null,null,null,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.i(TAG, "queryBook"+cursor.getColumnName(cursor.getColumnIndex("name")));
                Log.i(TAG,"queryBook"+cursor.getInt(cursor.getColumnIndex("page")));
            }
        }


    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f,1f);
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        triangle.draw();
    }
}
