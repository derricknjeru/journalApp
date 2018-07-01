package com.derrick.journalapp.contracts;

import android.support.annotation.NonNull;

import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.utilities.BasePresenter;
import com.derrick.journalapp.utilities.BaseView;

import java.util.List;

public interface JournalsContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showJournals(List<Journal> Journals);

        void showAddJournal(@NonNull String userId);

        void showJournalDetailsUi(long JournalId, String userId);

        void showLoadingJournalsError();

        void showNoJournals();

        boolean viewIsActive();

        void showSuccessFullySaved();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadJournals(boolean forceUpdate);

        void addNewJournal();

        void openJournalDetails(@NonNull Journal requestedJournal);

    }
}
