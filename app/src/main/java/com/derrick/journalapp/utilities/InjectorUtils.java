package com.derrick.journalapp.utilities;

import android.content.Context;
import android.support.annotation.NonNull;

import com.derrick.journalapp.data.source.JournalsRepository;
import com.derrick.journalapp.data.source.remote.JournalFireBaseDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class InjectorUtils {
    public static JournalsRepository provideJournalsRepository(@NonNull Context context) {
        checkNotNull(context);
        return JournalsRepository.getInstance(JournalFireBaseDataSource.getInstance());
    }
}
