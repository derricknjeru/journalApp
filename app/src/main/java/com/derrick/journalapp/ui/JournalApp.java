package com.derrick.journalapp.ui;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

public class JournalApp extends MultiDexApplication {
    private static FirebaseDatabase mDatabase;
    private static JournalApp mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        getFireBaseDatabase();
    }

    /*
     *this method returns application instance
     */

    public static synchronized JournalApp getInstance() {
        return mInstance;
    }

    public static FirebaseDatabase getFireBaseDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
