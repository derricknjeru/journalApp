package com.derrick.journalapp.contracts;

import com.derrick.journalapp.utilities.BasePresenter;
import com.derrick.journalapp.utilities.BaseView;

public interface JournalDetailContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingJournal();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showEditJournal(long journalId, String userId);

        void showJournalDeleted();

        boolean isActive();

        void hideTime();

        void showTime(String time);

        void hideDate();

        void showDate(String date);
    }

    interface Presenter extends BasePresenter {

        void editJournal();

        void deleteJournal();

    }
}
