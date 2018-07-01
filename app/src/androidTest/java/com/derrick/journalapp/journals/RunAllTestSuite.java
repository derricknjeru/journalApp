package com.derrick.journalapp.journals;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({JournalMainActivityUI.class,
        JournalsDetailsUITest.class})
public class RunAllTestSuite {
}
