package com.derrick.journalapp.journals;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.derrick.journalapp.R;
import com.derrick.journalapp.ui.JournalDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JournalsDetailsUITest {

    @Rule
    public ActivityTestRule<JournalDetailActivity> mActivityRule =
            new ActivityTestRule(JournalDetailActivity.class);

    @Test
    public void isDescriptionViewTextSet() {
        onView(withId(R.id.desc)).check(matches(withText(R.string.description)));
    }

    @Test
    public void isDateViewTextSet() {
        onView(withId(R.id.date)).check(matches(withText(R.string.date_time)));
    }


}
