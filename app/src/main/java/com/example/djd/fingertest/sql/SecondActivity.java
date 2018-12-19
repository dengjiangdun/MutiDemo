package com.example.djd.fingertest.sql;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.djd.fingertest.R;
import com.example.djd.fingertest.Triangle;
import com.example.djd.fingertest.WebViewActivitiy;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

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
    private LocalBroadcastManager mLocalBroadcastManager;
    private LocalBrocastReciver mLocalBrocastReciver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);
        MyDatabaseHepler myDatabaseHepler = new MyDatabaseHepler(this, "demo.db", null, 1);
       // db = myDatabaseHepler.getWritableDatabase("123123");
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addBook();
                Intent intent = new Intent(SecondActivity.this, WebViewActivitiy.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.johndon");
                mLocalBroadcastManager.sendBroadcast(intent);
                testdownloadManager();
            }
        });
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission});

        mGLV = findViewById(R.id.glv_test);

        mGLV.setRenderer(this);
        //myDatabaseHepler = new MyDatabaseHepler(this,"demo.db", null, 1);
        mLocalBroadcastManager =  LocalBroadcastManager.getInstance(this);
        mLocalBrocastReciver = new LocalBrocastReciver();
        IntentFilter intentFilter = new IntentFilter("com.android.johndon");
        mLocalBroadcastManager.registerReceiver(mLocalBrocastReciver, intentFilter);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  DownloadManager
        mLocalBroadcastManager.unregisterReceiver(mLocalBrocastReciver);
        if (isRegisterReceiver) {
            unregisterReceiver(receiver);
        }
    }


    class LocalBrocastReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(context, "local reciver"+action, Toast.LENGTH_SHORT).show();
        }
    }

    private long downManagerId = 0;
    private DownloadManager mDownloadManager;
    private String url = "https://aq.qq.com/cn2/manage/mbtoken/mbtoken_download?Android=1&flow_id=1007";
    private  boolean isRegisterReceiver = false;
    private DownloadReceiver receiver;
    private void setReceiver() {
        if (!isRegisterReceiver) {
            receiver = new DownloadReceiver();
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            this.registerReceiver(receiver, intentFilter);
            isRegisterReceiver = true;
        }
    }

    private void testdownloadManager(){
        path = getExternalCacheDir().getPath()+"1.apk";
        Log.i("testdownloadManager",path);
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (downManagerId != 0) {
            mDownloadManager.remove(downManagerId);
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading...");
        request.setDescription("awing");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            request.setRequiresDeviceIdle(false);
            request.setRequiresCharging(false);
        }

        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(new File(path)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
        }

        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
        downManagerId = mDownloadManager.enqueue(request);
        setReceiver();
    }

    String path;// = getExternalCacheDir().getPath();
    public class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installApk(context, id, path/*PublicUtile.getApkDownPath(context) + PublicUtile.TestApkName*/);
            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //处理 如果还未完成下载，用户点击Notification ，跳转到下载中心
                Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(viewDownloadIntent);
            }
        }

        /**
         * 启动安装
         *
         * @param context
         * @param downloadApkId
         * @param apkPath
         */
        private  void installApk(Context context, long downloadApkId, String apkPath) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                Log.e("DownloadManager", downloadFileUri.toString());
                install.setDataAndType(/*Uri.parse(apkPath)*/downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Log.e("DownloadManager", "download error");
            }
        }
    }

}
