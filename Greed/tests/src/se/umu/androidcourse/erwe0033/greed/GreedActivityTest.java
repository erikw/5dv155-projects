package se.umu.androidcourse.erwe0033.greed;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class se.umu.androidcourse.erwe0033.greed.GreedActivityTest \
 * se.umu.androidcourse.erwe0033.greed.tests/android.test.InstrumentationTestRunner
 */
public class GreedActivityTest extends ActivityInstrumentationTestCase2<GreedActivity> {

    public GreedActivityTest() {
        super("se.umu.androidcourse.erwe0033.greed", GreedActivity.class);
    }

}
