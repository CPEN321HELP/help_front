package com.example.help_m5.manageFacilityTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.FacilityActivity;
import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

public class ReviewPostTests {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), FacilityActivity.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        intent.putExtra("userEmail", account.getEmail());
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(account.getEmail());
        intent.putExtra("facility_id", "6");
        intent.putExtra("facilityType", 3);
        intent.putExtra("facility_json", "{\"_id\":6,\"facility\":{\"facilityType\":\"restaurants\",\"facility_status\":\"normal\",\"facilityTitle\":\"McDonald's\",\"facilityDescription\":\"Famous fast food restaurant that serves burgers, fries, soft drinks, and a variety of other fast food options. \",\"facilityImageLink\":\"https:\\/\\/s3-media0.fl.yelpcdn.com\\/bphoto\\/13GWBclQVEzXzkMkxZXIRA\\/o.jpg\",\"facilityOverallRate\":0,\"numberOfRates\":0,\"timeAdded\":\"2022\\/6\\/11\",\"longitude\":-123.24253759999999,\"latitude\":49.266646699999995},\"rated_user\":[],\"reviews\":[],\"adderID\":\"\"}");
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<FacilityActivity> mActivityRule =
            new ActivityScenarioRule<FacilityActivity>(intent);

    @Test
    public void testCommentAndCancelButton() {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.cancel_button_review)).perform(click());
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptySubmission() {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button)).perform(click());
        onView(withText("Please do not submit an empty form")).inRoot(new ToastMatcher())
                .check(matches(withText("Please do not submit an empty form")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testFullSubmission() throws InterruptedException {
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Very good! Great overall experience"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button)).perform(click());

        onView(withText("Success!")).inRoot(new ToastMatcher())
                .check(matches(withText("Success!")));

        Thread.sleep(2000);

        onView(withId(R.id.facilityNumberOfRates)).check(matches(withText(containsString("1"))));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Very good! Great overall experience"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button)).perform(click());

        onView(withText("You have commented in the past.")).inRoot(new ToastMatcher())
                .check(matches(withText("You have commented in the past.")));

        Thread.sleep(2000);

        onView(withId(R.id.facilityNumberOfRates)).check(matches(withText(containsString("1"))));

    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

}
