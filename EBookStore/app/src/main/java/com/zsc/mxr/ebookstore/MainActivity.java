package com.zsc.mxr.ebookstore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zsc.mxr.ebookstore.Utils.Cryption;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private static final String OKHTTP_DEBUGTAG = "OKHTTP_DEBUGTAG";
    private static final String Base64_DEBUGTAG = "Base64_DEBUGTAG";


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

        testCryption();
    }
    
    /*测试Base64加解密*/
    private void testCryption() {
        String originalStr = "{\"matchUserId\":964432,\"matchUserLogo\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/SA3CSnticeERMibaA5jxQbB0vRTpwBg81Vc4NsPvKDjr0PibibPPH7V9kSibACiasr8ordBRfOAmia3GhXVAT9K9WvOvA\\/132\",\"matchAccuracy\":12,\"matchUserName\":\"欣\",\"questionDetail\":[{\"questionId\":\"1715\",\"isRight\":0,\"answerIds\":[]},{\"questionId\":\"1714\",\"isRight\":0,\"answerIds\":[]},{\"questionId\":\"1716\",\"isRight\":0,\"answerIds\":[]},{\"questionId\":\"1718\",\"isRight\":0,\"answerIds\":[]},{\"questionId\":\"1713\",\"isRight\":0,\"answerIds\":[6502]},{\"questionId\":\"1719\",\"isRight\":1,\"answerIds\":[6520]},{\"questionId\":\"1712\",\"isRight\":1,\"answerIds\":[6499]},{\"questionId\":\"1717\",\"isRight\":0,\"answerIds\":[6514]}],\"qaInfoId\":\"261\",\"endTime\":1562918775863,\"ip\":\"fe80::cb8:eb8:a9b4:2a0\",\"accuracy\":25,\"result\":1,\"startTime\":1562918652000,\"isPk\":0}";
        String encodedStr = Cryption.encryptionToStr(originalStr, true);
        String decodedStr = Cryption.decryption(encodedStr);
        Log.d(Base64_DEBUGTAG, decodedStr);
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
