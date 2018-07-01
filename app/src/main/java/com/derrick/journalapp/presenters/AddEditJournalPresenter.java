package com.derrick.journalapp.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.derrick.journalapp.contracts.AddEditJournalContract;
import com.derrick.journalapp.data.source.JournalsDataSource;
import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.utilities.LogUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditJournalPresenter implements AddEditJournalContract.Presenter, JournalsDataSource.GetJournalCallback, JournalsDataSource.PostJournalCallback {

    private static final String LOG_TAG = AddEditJournalPresenter.class.getSimpleName();
    @NonNull
    private final JournalsDataSource mJournalRepository;

    @NonNull
    private final AddEditJournalContract.View mAddJournalView;

    @Nullable
    private long mJournalId;

    @NonNull
    private String mUserId;


    public AddEditJournalPresenter(@NonNull String userId, @Nullable long journalId, @NonNull JournalsDataSource journalsRepository,
                                   @NonNull AddEditJournalContract.View addTaskView) {
        mJournalId = journalId;

        mUserId = checkNotNull(userId);
        mJournalRepository = checkNotNull(journalsRepository);
        mAddJournalView = checkNotNull(addTaskView);

        mAddJournalView.setPresenter(this);
    }

    @Override
    public void saveJournal(Journal journal) {

        LogUtils.showLog(LOG_TAG, "@Fetched isNewJournal()::" + isNewJournal());
        LogUtils.showLog(LOG_TAG, "@Fetched mJournalId::" + mJournalId);

        if (isNewJournal()) {
            //posting a new journal to fireBase
            createJournal(journal);
        } else {
            //updating a journal to fireBase
            updateJournal(journal);
        }
    }

    private void updateJournal(Journal journal) {
        mJournalRepository.updateToFireBase(this, mUserId, mJournalId, journal);
        mAddJournalView.showJournalsList(); // After an edit, go back to the list.
    }

    private void createJournal(Journal journal) {
        if (journal.isEmpty()) {
            mAddJournalView.showEmptyJournalError();
        } else {
            mJournalRepository.postToFireBase(new JournalsDataSource.PostJournalCallback() {
                @Override
                public void onJournalPosted() {
                    mAddJournalView.showPostedSuccessFully();
                    mAddJournalView.showJournalsList();
                }

                @Override
                public void onJournalNotPosted(Exception e) {
                    mAddJournalView.showNotPosted();
                }
            }, mUserId, journal);
        }
    }

    @Override
    public void populateJournal() {
        mJournalRepository.getJournal(mJournalId, mUserId, this);
    }

    @Override
    public void onJournalLoaded(Journal mJournal) {
        LogUtils.showLog(LOG_TAG, "@Fetched onJournalLoaded::" + mJournal);
        if (mAddJournalView.isActive()) {
            mAddJournalView.setTitle(mJournal.getTitle());
            mAddJournalView.setDescription(mJournal.getDescription());
            mAddJournalView.setDate(mJournal.getDate());
            mAddJournalView.setTime(mJournal.getTime());
        }
    }

    @Override
    public void onDataNotAvailable() {
        if (mAddJournalView.isActive()) {
            mAddJournalView.showEmptyJournalError();
        }
    }

    @Override
    public void start() {
        if (!isNewJournal()) {
            populateJournal();
        }
    }

    private boolean isNewJournal() {
        return mJournalId == 0;
    }

    @Override
    public void onJournalPosted() {
        mAddJournalView.showPostedSuccessFully();
        mAddJournalView.showJournalsList();
    }

    @Override
    public void onJournalNotPosted(Exception e) {
        mAddJournalView.showNotPosted();
    }
}
