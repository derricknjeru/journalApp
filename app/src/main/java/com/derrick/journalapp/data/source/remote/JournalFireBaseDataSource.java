package com.derrick.journalapp.data.source.remote;

import android.support.annotation.NonNull;

import com.derrick.journalapp.data.source.JournalsDataSource;
import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.ui.JournalApp;
import com.derrick.journalapp.utilities.LogUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalFireBaseDataSource implements JournalsDataSource {

    private static final String LOG_TAG = JournalFireBaseDataSource.class.getSimpleName();
    private static JournalFireBaseDataSource INSTANCE;
    private String JOURNALS = "journals";

    public static JournalFireBaseDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JournalFireBaseDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void deleteTask(long mJournalId, String userId, @NonNull final DeleteJournalCallback callback) {
        DatabaseReference ref = JournalApp.getInstance().getFireBaseDatabase().getReference(JOURNALS).child(userId);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("orderID").equalTo(mJournalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    String key = snap.getKey();
                    dataSnapshot.getRef().child(key).removeValue();

                    callback.onJournalDeleted();

                    LogUtils.showLog(LOG_TAG, "@Deleted key::" + key);
                }


            }

            @Override
            public void onCancelled(DatabaseError e) {
                callback.onJournalNotDeleted(e.toException());

            }
        });
    }

    @Override
    public void getJournals(@NonNull final LoadJournalsCallback callback, @NonNull String userId) {
        loadAllJournals(callback, userId);
    }

    @Override
    public void getJournal(@NonNull long journalId, String mUserId, @NonNull GetJournalCallback callback) {
        loadJournal(journalId, mUserId, callback);
    }

    private void loadJournal(long journalId, String userId, final GetJournalCallback callback) {

        DatabaseReference ref = JournalApp.getInstance().getFireBaseDatabase().getReference(JOURNALS).child(userId);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("orderID").equalTo(journalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Journal journal = snap.getValue(Journal.class);
                    callback.onJournalLoaded(journal);

                    LogUtils.showLog(LOG_TAG, "@Fetched firebasejournalJournal::" + journal);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtils.showLog(LOG_TAG, "The read failed" + databaseError.getCode());
                callback.onDataNotAvailable();

            }
        });
    }

    @Override
    public void postToFireBase(@NonNull final PostJournalCallback callback, String userId, Journal journal) {
        JournalApp.getInstance().getFireBaseDatabase().getReference().child(JOURNALS).child(userId).push().setValue(journal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onJournalPosted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onJournalNotPosted(e);
            }
        });
    }

    @Override
    public void updateToFireBase(@NonNull final PostJournalCallback callback, String userId, long key, final Journal journal) {

        DatabaseReference ref = JournalApp.getInstance().getFireBaseDatabase().getReference(JOURNALS).child(userId);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("orderID").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    String key = snap.getKey();


                    Map<String, Object> journalMap = new HashMap<>();

                    journalMap.put("title", journal.getTitle());
                    journalMap.put("description", journal.getDescription());
                    journalMap.put("time", journal.getTime());
                    journalMap.put("date", journal.getDate());

                    dataSnapshot.getRef().child(key).updateChildren(journalMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.onJournalPosted();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onJournalNotPosted(e);
                        }
                    });


                }


            }

            @Override
            public void onCancelled(DatabaseError e) {
                callback.onJournalNotPosted(e.toException());

            }
        });


    }

    /**
     * fetches journals from firebase database
     *
     * @param callback
     * @param userId
     */
    private void loadAllJournals(@NonNull final LoadJournalsCallback callback, @NonNull String userId) {
        DatabaseReference ref = JournalApp.getInstance().getFireBaseDatabase().getReference(JOURNALS).child(userId);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("orderID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Journal> journalList = new ArrayList<>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Journal journal = snap.getValue(Journal.class);
                    journalList.add(journal);
                }

                callback.onJournalsLoaded(journalList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtils.showLog(LOG_TAG, "The read failed" + databaseError.getCode());
                callback.onDataNotAvailable();

            }
        });
    }


}
