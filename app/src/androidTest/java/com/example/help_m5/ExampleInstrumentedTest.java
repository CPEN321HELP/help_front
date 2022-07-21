package com.example.help_m5;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
/*
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.help_m5", appContext.getPackageName());


    }
}
*/


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest  {

    @Before
    public void setUp() {
        ActivityScenario<FacilityActivity> mfragment = ActivityScenario.launch(FacilityActivity.class);
        mfragment.moveToState(Lifecycle.State.STARTED);
    }

    @Rule
    public ActivityTestRule<FacilityActivity> activityRule =
            new ActivityTestRule<>(FacilityActivity.class);

    @Test
    public void listGoesOverTheFold() {
        onView(withId(R.id.rate_button)).check(matches(isDisplayed()));
    }
}