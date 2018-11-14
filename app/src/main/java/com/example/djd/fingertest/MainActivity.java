package com.example.djd.fingertest;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity {
    private static int sBinderCount = 0;

    private ServiceConnection mScon;
    private static final String TAG = "Test_MainActivity";
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2 ){
                Log.i("handler","handlerMessage");
            }
        }
    };

    private TextView mTvShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Debug.startMethodTracing();
        if (Build.VERSION.SDK_INT > 23){
            //initKey();
            //initCiper();
        }
        mScon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };



        Log.i("main", "onCreate: "+Build.VERSION.SDK_INT);

        findViewById(R.id.tv_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
                testHandler();
            }
        });



        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService();
            }
        });

        testHandler();
        mTvShow = (TextView) findViewById(R.id.tv_show);
        mTvShow.setText("appid"+BuildConfig.appid+"key"+BuildConfig.key+"packagename"+BuildConfig.APPLICATION_ID);




        findViewById(R.id.btn_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
                Log.i(TAG, "alram");
                writeToExternalStorage();
            }
        });

        testRxJava();
        testOkhttp();
        testRxJavaPost();
      //  Debug.stopMethodTracing();
    }


    public void writeToExternalStorage() {
        File externalStorage = Environment.getExternalStorageDirectory();
        File destFile = new File(externalStorage, "dest.txt");
        try {
            OutputStream output = new FileOutputStream(destFile, true);
            output.write("droidyue.com".getBytes());
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("intent_alarm_log");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent, 0);
        Long time =  1000L;
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


    private void bindService(){
        Intent intent = new Intent(getApplication(),MyServices.class);
        bindService(intent,mScon, Context.BIND_AUTO_CREATE);
        sBinderCount++;
        Log.i(TAG, "bindService: count"+sBinderCount);
    }


    private void unbindService(){
        if (sBinderCount <= 0) {
            return;
        }

        unbindService(mScon);
        sBinderCount--;
        Log.i(TAG, "unbindService: count"+sBinderCount);
    }


    KeyStore keyStore;
    @TargetApi(23)
    private void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("default_key",
                    KeyProperties.PURPOSE_ENCRYPT |KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCiper() {
        try {

            SecretKey key = (SecretKey) keyStore.getKey("default_key",null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+
            KeyProperties.BLOCK_MODE_CBC+"/"
            +KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            showFingerPrintDialog(cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showFingerPrintDialog(Cipher cipher) {
        FingerprintDialogFragment fingerprintDialogFragment = new FingerprintDialogFragment();
        fingerprintDialogFragment.setChipher(cipher);
        fingerprintDialogFragment.show(getFragmentManager(),"fingerprint");
    }

    private void testHandler() {

        Log.i("handler","testHandler");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(2);
                waitDone();
            }
        }).start();

    }


    public boolean waitDone() {
        Log.i("handler","waitDonenotifystart");
        final Object waitDoneLock = new Object();
        final Runnable unlockRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (waitDoneLock) {
                    Log.i("handler","waitDonenotifyAll");
                    waitDoneLock.notifyAll();
                }
            }
        };

        synchronized (waitDoneLock) {
            mHandler.post(unlockRunnable);
            try {
                Log.i("handler","waitDonewait before");
                waitDoneLock.wait();
                Log.i("handler","waitDonewait after");
            } catch (InterruptedException ex) {
                Log.v(TAG, "waitDone interrupted");
                return false;
            }
        }
        Log.i("handler", "waitDone return: ");
        return true;
    }

    private void testRxJava() {
       /* Observable<String> novel = */Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("johndon");
                e.onNext("johndondeng");
                e.onComplete();
            }
        }).subscribe(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.i(TAG,"onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i(TAG,"onComplete");
            }
        });

       Observable<String> novel = Observable.create(new ObservableOnSubscribe<String>() {
           @Override
           public void subscribe(ObservableEmitter<String> e) throws Exception {
               Thread.sleep(1000);
               e.onNext("1s");
               Thread.sleep(1000);
               e.onNext("2s");
               Thread.sleep(1000);
               e.onNext("3s");
           }
       });

        io.reactivex.Observer observer = new io.reactivex.Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                Log.i(TAG,"countdown"+(String)value+"thread name"+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        novel.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    private void testOkhttp() {
        Request request  = new Request.Builder().url(" http://www.publicobject.com/helloworld.txt").get().build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LogIntercepter()).build();
        Call call  = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"onResponse"+response.body().string());
            }
        });
    }

    class LogIntercepter implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.i(TAG, "intercept method"+request.method()+"header"+request.headers()+"url"+request.url());
            Response response = chain.proceed(request);
            Log.i(TAG, "intercept response body"+response.body()+"\n"+response.headers()+"url"+response.request().url());
            return response;
        }
    }


    private void testRxJavaPost() {
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("hello word");
            }
        };

        final Request request = new Request.Builder().url("https://api.github.com/markdown/raw").
                post(requestBody).build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"post onResponse \n"+response.body().string());
            }
        });
    }


}
