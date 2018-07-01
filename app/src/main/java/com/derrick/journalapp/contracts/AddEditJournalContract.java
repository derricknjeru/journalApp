package com.derrick.journalapp.contracts;

import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.utilities.BasePresenter;
import com.derrick.journalapp.utilities.BaseView;

public interface AddEditJournalContract {
    interface View extends BaseView<Presenter> {

        void showEmptyJournalError();

        void showPostedSuccessFully();

        void showNotPosted();

        void showJournalsList();

        void setTitle(String title);

        void setDate(String date);

        void setTime(String time);

        void setDescription(String description);


        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveJournal(Journal journal);

        void populateJournal();


    }
}
