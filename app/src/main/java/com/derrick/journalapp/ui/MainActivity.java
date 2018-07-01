package com.derrick.journalapp.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.derrick.journalapp.R;
import com.derrick.journalapp.fragments.MainFragment;
import com.derrick.journalapp.presenters.JournalsPresenter;
import com.derrick.journalapp.utilities.ActivityUtils;
import com.derrick.journalapp.utilities.GlideApp;
import com.derrick.journalapp.utilities.InjectorUtils;
import com.derrick.journalapp.utilities.LogUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    private String mUsername, mPhotoUrl;

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";

    private JournalsPresenter mJournalsPresenter;
    private CircleImageView mProfileImageView;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (mainFragment == null) {
            // Create the fragment
            mainFragment = MainFragment.newInstance("", "");
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mainFragment, R.id.contentFrame);
        }


        // Initialize Firebase Auth
        initializeAuth();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Create the presenter
        mJournalsPresenter = new JournalsPresenter(mainFragment,
                InjectorUtils.provideJournalsRepository(getApplicationContext()), mUserId);

        if (mPhotoUrl != null) {
            setProfileImage();
        }


    }

    private void setProfileImage() {
        mProfileImageView = findViewById(R.id.profile_image);
        GlideApp
                .with(this)
                .load(mPhotoUrl)
                .centerCrop()
                .into(mProfileImageView);
    }

    private void setProfileMessage(String name) {
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String message = "";
        if (!Strings.isNullOrEmpty(name)) {
            if (timeOfDay >= 0 && timeOfDay < 12) {
                message = "Morning, " + name;

            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                message = "Afternoon, " + name;
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                message = "Evening, " + name;

            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                message = "Hello, " + name;
            }

            if (!Strings.isNullOrEmpty(message)) {
                collapsingToolbar.setTitle(message);
            } else {
                collapsingToolbar.setTitle(getString(R.string.app_name));
            }
        } else {
            collapsingToolbar.setTitle(getString(R.string.app_name));
        }


    }


    private void initializeAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mUserId = mFirebaseUser.getUid();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

            setProfileMessage(mUsername);

            LogUtils.showLog(LOG_TAG, "@user mPhotoUrl::" + mPhotoUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
