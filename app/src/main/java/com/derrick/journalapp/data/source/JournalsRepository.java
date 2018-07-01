package com.derrick.journalapp.data.source;

import android.support.annotation.NonNull;

import com.derrick.journalapp.pojos.Journal;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class JournalsRepository implements JournalsDataSource {

    private static JournalsRepository INSTANCE = null;

    private final JournalsDataSource mJournalFireBaseDataSource;

    public JournalsRepository(@NonNull JournalsDataSource journalsDataSource) {
        this.mJournalFireBaseDataSource = checkNotNull(journalsDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary,
     *
     * @param journalFireBaseDataSource
     * @return {@link JournalsRepository} instance
     */
    public static JournalsRepository getInstance(JournalsDataSource journalFireBaseDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new JournalsRepository(journalFireBaseDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void deleteTask(long mJournalId, String userId, @NonNull final DeleteJournalCallback callback) {
        mJournalFireBaseDataSource.deleteTask(mJournalId, userId, new DeleteJournalCallback() {
            @Override
            public void onJournalDeleted() {
                callback.onJournalDeleted();
            }

            @Override
            public void onJournalNotDeleted(Exception e) {
                callback.onJournalNotDeleted(e);
            }
        });
    }

    @Override
    public void getJournals(@NonNull LoadJournalsCallback callback, @NonNull String userId) {
        checkNotNull(callback);
        getJournalFromFireBase(callback, userId);
    }

    private void getJournalFromFireBase(final LoadJournalsCallback callback, @NonNull String userId) {
        mJournalFireBaseDataSource.getJournals(new LoadJournalsCallback() {
            @Override
            public void onJournalsLoaded(List<Journal> journals) {
                callback.onJournalsLoaded(journals);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, userId);
    }

    @Override
    public void getJournal(@NonNull long JournalId, String mUserId, @NonNull final GetJournalCallback callback) {

        mJournalFireBaseDataSource.getJournal(JournalId, mUserId, new GetJournalCallback() {
            @Override
            public void onJournalLoaded(Journal journal) {
                callback.onJournalLoaded(journal);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * This method calls{@link com.derrick.journalapp.data.source.remote.JournalFireBaseDataSource} that is responsible for posting to firebase
     *
     * @param callback
     * @param userId
     * @param journal
     */
    @Override
    public void postToFireBase(@NonNull final PostJournalCallback callback, String userId, Journal journal) {
        mJournalFireBaseDataSource.postToFireBase(new PostJournalCallback() {
            @Override
            public void onJournalPosted() {
                callback.onJournalPosted();
            }

            @Override
            public void onJournalNotPosted(Exception e) {
                callback.onJournalNotPosted(e);
            }
        }, userId, journal);
    }

    @Override
    public void updateToFireBase(@NonNull final PostJournalCallback callback, String userId, long key, Journal journal) {
        mJournalFireBaseDataSource.updateToFireBase(new PostJournalCallback() {
            @Override
            public void onJournalPosted() {
                callback.onJournalPosted();
            }

            @Override
            public void onJournalNotPosted(Exception e) {
                callback.onJournalNotPosted(e);
            }
        }, userId, key, journal);
    }


}
