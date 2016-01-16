package com.os.operando.chromeapp;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CustomTabs";
    private static final Uri URI = Uri.parse("https://html5test.com/");

    private CustomTabsServiceConnection mCustomTabsServiceConnection;
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsSession mCustomTabsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                Log.d(TAG, "onCustomTabsServiceConnected");
                mCustomTabsClient = customTabsClient;
                // Chrome と接続したらWarm upする
                mCustomTabsClient.warmup(0L);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
            }
        };

        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        // Chrome と接続
        CustomTabsClient.bindCustomTabsService(this, packageName, mCustomTabsServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mCustomTabsServiceConnection);
    }

    public void onClick(View v) {
        launchUrl();
    }

    public void onWebView(View v) {
        startActivity(WebViewActivity.createIntent(this, URI.toString()));
    }

    public void onPreFetch(View v) {
        //  NavigationEventが必要ない場合は引数にnullを指定
        //  mCustomTabsSession = mCustomTabsClient.newSession(null);
        mCustomTabsSession = mCustomTabsClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(final int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
                Log.d(TAG, "onNavigationEvent : " + navigationEvent);
                Log.d(TAG, "extras : " + extras != null ? extras.toString() : "");

                // CustomTabsCallbackはCustomTab表示時のナビゲーションイベントを取得できる
                //   NAVIGATION_STARTED = 1;  読み込み開始したとき
                //   NAVIGATION_FINISHED = 2; 読み込み終了したとき
                //   NAVIGATION_FAILED = 3;
                //   NAVIGATION_ABORTED = 4;
                //   TAB_SHOWN = 5;  Tabが表示されたとき
                //   TAB_HIDDEN = 6; Tab閉じたとき

                switch (navigationEvent) {
                    case NAVIGATION_STARTED:
                        break;
                    case NAVIGATION_FINISHED:
                        break;
                    case NAVIGATION_FAILED:
                        break;
                    case NAVIGATION_ABORTED:
                        break;
                    case TAB_SHOWN:
                        break;
                    case TAB_HIDDEN:
                        break;
                }
            }
        });
        boolean isSuccess = mCustomTabsSession.mayLaunchUrl(URI, null, null);
        Log.d(TAG, "isSuccess : " + isSuccess);
        launchUrl(mCustomTabsSession);
    }

    void launchUrl() {
        launchUrl(null);
    }

    void launchUrl(CustomTabsSession customTabsSession) {
        CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder(customTabsSession)
                .setShowTitle(true)
                .setToolbarColor(0xff0000)
                .enableUrlBarHiding()
                .build();
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        tabsIntent.intent.setPackage(packageName);
        tabsIntent.launchUrl(this, URI);
    }
}