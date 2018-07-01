package com.derrick.journalapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.derrick.journalapp.R;
import com.derrick.journalapp.fragments.JournalDetailFragment;
import com.derrick.journalapp.presenters.AddEditJournalPresenter;
import com.derrick.journalapp.presenters.JournalDetailPresenter;
import com.derrick.journalapp.utilities.ActivityUtils;
import com.derrick.journalapp.utilities.InjectorUtils;
import com.derrick.journalapp.utilities.LogUtils;

public class JournalDetailActivity extends AppCompatActivity {

    public static final String EXTRA_JOURNAL_ID = "JOURNAL_ID";

    public static final String EXTRA_USER_ID = "USER_ID";
    private static final String LOG_TAG = JournalDetailActivity.class.getSimpleName();

    private JournalDetailPresenter mAddEditJournalPresenter;
    JournalDetailFragment JournalDetailFragment;
    private ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        mActionBar.setTitle("");

        // Get the requested Journal id
        long journalId = getIntent().getLongExtra(EXTRA_JOURNAL_ID, 0);

        // Get the user id
        String userId = getIntent().getStringExtra(EXTRA_USER_ID);


        JournalDetailFragment = (JournalDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (JournalDetailFragment == null) {
            JournalDetailFragment = JournalDetailFragment.newInstance("" + journalId, userId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    JournalDetailFragment, R.id.contentFrame);
        }


        new JournalDetailPresenter(InjectorUtils.provideJournalsRepository(getApplicationContext()),
                JournalDetailFragment, journalId, userId);

        LogUtils.showLog(LOG_TAG, "@Fetched journalId2" + journalId);
        LogUtils.showLog(LOG_TAG, "@Fetched userId" + userId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        onBackPressed();
        return true;
    }
}
