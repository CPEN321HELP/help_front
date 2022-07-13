package com.example.help_m5;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.help_m5.ui.add_facility.AddFacilityFragment;

import org.junit.Before;
import org.junit.Test;

public class AddFacilityFragmentTest {

    @Before
    public void setUp() throws Exception {
        FragmentScenario<AddFacilityFragment> mfragment = FragmentScenario.launchInContainer(AddFacilityFragment.class);
        mfragment.moveToState(Lifecycle.State.STARTED);
    }
    @Test
    public void t1(){
        onView(withId(R.id.newFacilityTitle)).perform(typeText("Sve"));
        onView(withId(R.id.imageNewFacilityTitle)).check(matches(withTagValue(equalTo("bad"))));
    }
    @Test
    public void t2(){
        onView(withId(R.id.newFacilityTitle)).perform(typeText("Svseev"));
        onView(withId(R.id.imageNewFacilityTitle)).check(matches(withTagValue(equalTo("good"))));
    }
}