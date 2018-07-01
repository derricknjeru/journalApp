package com.derrick.journalapp.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.derrick.journalapp.R;
import com.derrick.journalapp.fragments.AddEditJournalFragment;
import com.derrick.journalapp.presenters.AddEditJournalPresenter;
import com.derrick.journalapp.utilities.ActivityUtils;
import com.derrick.journalapp.utilities.InjectorUtils;

import javax.annotation.Nullable;

public class AddEditJournalActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_JOURNAL = 1;

    private ActionBar mActionBar;

    private AddEditJournalPresenter mAddEditJournalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);


        AddEditJournalFragment addEditTaskFragment = (AddEditJournalFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        long journalId = getIntent().getLongExtra(AddEditJournalFragment.ARGUMENT_EDIT_JOURNAL_ID,0);

        String userId = getIntent().getStringExtra(AddEditJournalFragment.ARGUMENT_USER_ID);
        /**
         * setting toolBar title. if id is not empty it's a new journal
         */
        setToolbarTitle(String.valueOf(journalId));

        /**
         * adding fragment AddEditJournalFragment to  {@link AddEditJournalActivityy}
         *
         */
        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditJournalFragment.newInstance("", "");
            //getting journal id and setting it to a bundle
            if (getIntent().hasExtra(AddEditJournalFragment.ARGUMENT_EDIT_JOURNAL_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditJournalFragment.ARGUMENT_EDIT_JOURNAL_ID, String.valueOf(journalId));
                addEditTaskFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }

        //creating presenter

        mAddEditJournalPresenter = new AddEditJournalPresenter(userId, journalId,
                InjectorUtils.provideJournalsRepository(getApplicationContext()), addEditTaskFragment);

    }

    private void setToolbarTitle(@Nullable String journalId) {
        if (journalId == null) {
            mActionBar.setTitle(R.string.add_journal);
        } else {
            mActionBar.setTitle(R.string.edit_journal);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}