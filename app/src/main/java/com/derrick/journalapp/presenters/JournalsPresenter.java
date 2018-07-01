package com.derrick.journalapp.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.derrick.journalapp.contracts.JournalsContract;
import com.derrick.journalapp.data.source.JournalsDataSource;
import com.derrick.journalapp.data.source.JournalsRepository;
import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.ui.AddEditJournalActivity;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author derrick
 * <p>
 * Listens to user actions from the UI ({@link com.derrick.journalapp.fragments.MainFragment}), retrieves the data and updates the
 * UI as required.
 */

public class JournalsPresenter implements JournalsContract.Presenter {

    private final JournalsContract.View mJournalsView;
    private final JournalsRepository mJournalsRepository;
    private String mUserId;


    public JournalsPresenter(@NonNull JournalsContract.View journalView, @NonNull JournalsRepository journalsRepository, String userId) {
        mJournalsView = checkNotNull(journalView, "journalView cannot be null!");
        mJournalsRepository = checkNotNull(journalsRepository);
        mUserId = userId;
        mJournalsView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditJournalActivity.REQUEST_ADD_JOURNAL == requestCode && Activity.RESULT_OK == resultCode) {
            mJournalsView.showSuccessFullySaved();
        }
    }

    @Override
    public void loadJournals(boolean forceUpdate) {
        loadRepositoryJournals(true);
    }

    private void loadRepositoryJournals(final boolean showLoadingUI) {

        if (showLoadingUI) {
            mJournalsView.setLoadingIndicator(true);
        }

        mJournalsRepository.getJournals(new JournalsDataSource.LoadJournalsCallback() {
            @Override
            public void onJournalsLoaded(List<Journal> journals) {
                if (!mJournalsView.viewIsActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mJournalsView.setLoadingIndicator(false);
                }

                processJournals(journals);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mJournalsView.viewIsActive()) {
                    return;
                }
                mJournalsView.showLoadingJournalsError();
            }
        }, mUserId);

    }

    private void processJournals(List<Journal> journals) {
        if (journals != null && journals.size() > 0) {
            mJournalsView.showJournals(journals);
        } else {
            mJournalsView.showNoJournals();
        }
    }

    @Override
    public void addNewJournal() {
        mJournalsView.showAddJournal(mUserId);
    }

    @Override
    public void openJournalDetails(@NonNull Journal requestedJournal) {
        checkNotNull(requestedJournal, "requestedTask cannot be null!");

        mJournalsView.showJournalDetailsUi(requestedJournal.getOrderID(),mUserId);
    }

    @Override
    public void start() {
        loadJournals(false);
    }
}
