package com.zsc.mxr.ebookstore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private String OKHTTP_DEBUGTAG = "OKHTTP_DEBUGTAG";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        testOkHttpAsyncGetRequest();
//        testOkHttpSyncGetRequest();
    }

    /*异步GET请求*/
    private void testOkHttpAsyncGetRequest() {
        String url = "http://www.baidu.com";
        OkHttpClient httpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        Call call =  httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(OKHTTP_DEBUGTAG, e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(OKHTTP_DEBUGTAG, response.body().string());
            }
        });
    }

    /*同步GET请求,该方法会阻塞调用线程*/
    private void testOkHttpSyncGetRequest() {
        String url = "http://www.baidu.com";
        OkHttpClient httpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        final Call call =  httpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    Log.d(OKHTTP_DEBUGTAG, response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
