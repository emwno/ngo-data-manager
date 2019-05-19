package com.emwno.ngo.datamanager;

import android.app.Application;

import com.backendless.Backendless;

import io.realm.Realm;

/**
 * Created on 19 Jul 2018.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(this, BuildConfig.BACKENDLESS_APP_ID, BuildConfig.BACKENDLESS_API_KEY);
        Realm.init(this);
    }
}
