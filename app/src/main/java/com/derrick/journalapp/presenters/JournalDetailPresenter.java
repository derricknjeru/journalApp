package com.derrick.journalapp.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.derrick.journalapp.contracts.AddEditJournalContract;
import com.derrick.journalapp.contracts.JournalDetailContract;
import com.derrick.journalapp.data.source.JournalsDataSource;
import com.derrick.journalapp.pojos.Journal;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkNotNull;

public class JournalDetailPresenter implements JournalDetailContract.Presenter {

    @NonNull
    private final JournalsDataSource mJournalRepository;
    @NonNull
    private final JournalDetailContract.View mJournalDetailView;

    @Nullable
    private long mJournalId;

    @Nullable
    private String mUserId;

    public JournalDetailPresenter(@NonNull JournalsDataSource mJournalRepository, @NonNull JournalDetailContract.View mJournalDetailView, long mJournalId, String userId) {
        this.mJournalRepository = checkNotNull(mJournalRepository, "journalsRepository cannot be null!");
        this.mJournalDetailView = checkNotNull(mJournalDetailView, "journalsRepository cannot be null!");
        this.mJournalId = mJournalId;
        this.mUserId = userId;

        mJournalDetailView.setPresenter(this);
    }

    @Override
    public void editJournal() {
        if (mJournalId == 0) {
            mJournalDetailView.showMissingJournal();
            return;
        }
        mJournalDetailView.showEditJournal(mJournalId, mUserId);
    }

    @Override
    public void deleteJournal() {
        if (mJournalId == 0) {
            mJournalDetailView.showMissingJournal();
            return;
        }
        mJournalRepository.deleteTask(mJournalId, mUserId, new JournalsDataSource.DeleteJournalCallback() {
            @Override
            public void onJournalDeleted() {
                mJournalDetailView.showJournalDeleted();
            }

            @Override
            public void onJournalNotDeleted(Exception e) {

            }
        });

    }

    @Override
    public void start() {
        openJournal();
    }

    private void openJournal() {
        if (mJournalId == 0) {
            mJournalDetailView.showMissingJournal();
            return;
        }

        mJournalDetailView.setLoadingIndicator(true);

        mJournalRepository.getJournal(mJournalId, mUserId, new JournalsDataSource.GetJournalCallback() {
            @Override
            public void onJournalLoaded(Journal journal) {
                // The view may not be able to handle UI updates anymore
                if (!mJournalDetailView.isActive()) {
                    return;
                }
                mJournalDetailView.setLoadingIndicator(false);
                if (null == journal) {
                    mJournalDetailView.showMissingJournal();
                } else {
                    showJournal(journal);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mJournalDetailView.isActive()) {
                    return;
                }
                mJournalDetailView.showMissingJournal();

            }
        });
    }

    private void showJournal(Journal journal) {

        String title = journal.getTitle();
        String description = journal.getDescription();

        String time = journal.getTime();
        String date = journal.getDate();

        if (Strings.isNullOrEmpty(title)) {
            mJournalDetailView.hideTitle();
        } else {
            mJournalDetailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mJournalDetailView.hideDescription();
        } else {
            mJournalDetailView.showDescription(description);
        }

        if (Strings.isNullOrEmpty(time)) {
            mJournalDetailView.hideTime();
        } else {
            mJournalDetailView.showTime(time);
        }

        if (Strings.isNullOrEmpty(date)) {
            mJournalDetailView.hideDate();
        } else {
            mJournalDetailView.showDate(date);
        }
    }
}
