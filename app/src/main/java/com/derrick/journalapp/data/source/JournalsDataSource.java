package com.derrick.journalapp.data.source;

import android.support.annotation.NonNull;

import com.derrick.journalapp.pojos.Journal;

import java.util.List;

/**
 * @author derrick
 */
public interface JournalsDataSource {


    interface LoadJournalsCallback {

        void onJournalsLoaded(List<Journal> Journals);

        void onDataNotAvailable();
    }

    interface GetJournalCallback {

        void onJournalLoaded(Journal Journal);

        void onDataNotAvailable();
    }

    interface PostJournalCallback {

        void onJournalPosted();

        void onJournalNotPosted(Exception e);
    }

    interface DeleteJournalCallback {

        void onJournalDeleted();

        void onJournalNotDeleted(Exception e);
    }

    void deleteTask(long mJournalId, String userId, @NonNull DeleteJournalCallback callback);

    void getJournals(@NonNull LoadJournalsCallback callback, String userId);

    void getJournal(@NonNull long JournalId, String mUserId, @NonNull GetJournalCallback callback);

    void postToFireBase(@NonNull PostJournalCallback callback, String userId, Journal journal);

    void updateToFireBase(@NonNull PostJournalCallback callback, String userId, long key, Journal journal);
}
