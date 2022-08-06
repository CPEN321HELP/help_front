package com.example.help_m5.review_facility_tests;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.help_m5.MainActivity;
import com.example.help_m5.R;
import com.example.help_m5.ToastMatcher;
import com.example.help_m5.SetRatingHelper;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class ReviewFacilityTests {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<MainActivity>(intent);

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("user_email", "test@gmail.com");
        intent.putExtra("user_name", "name");
        intent.putExtra("user_icon", "https://cdn.discordapp.com/attachments/984213736652935230/1004501216581136414/unknown.png");
        intent.putExtras(bundle);
    }

    private boolean spinnerChangeIndex(int indexSpinner){
        try{
            onView(withId(R.id.spinnerFacility)).perform(click());
            onData(Matchers.anything()).atPosition(indexSpinner).perform(click());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void navigateToRate() throws InterruptedException {
        onView(withId(R.id.home_review_button)).perform(click());
        Thread.sleep(1500);
        Assert.assertTrue(spinnerChangeIndex(2));
        Thread.sleep(500);
        onView(withId(R.id.facility3)).perform(click());
        Thread.sleep(1500);
    }

    @Test
    public void testButtonsAndLayout() throws InterruptedException {

        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));
        onView(withId(R.id.RateFacilityTitle)).check(matches(withText("Rate this Facility")));
        onView(withId(R.id.RateFacilityDescription))
                .check(matches(withText("Please select some stars and give\nyour feedback")));
        onView(withId(R.id.ratingBar2)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.submit_button_review)).check(matches(isEnabled()));
        onView(withId(R.id.cancel_button_review)).perform(click());
        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptySubmission() throws InterruptedException {
        navigateToRate();
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please do not submit an empty form")).inRoot(new ToastMatcher())
                .check(matches(withText("Please do not submit an empty form")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionRating() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please rate the facility from 0.5 to 5")).inRoot(new ToastMatcher())
                .check(matches(withText("Please rate the facility from 0.5 to 5")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testPartialSubmissionComment() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRatingHelper.setRatingBar());
        onView(withId(R.id.submit_button_review)).perform(click());
        onView(withText("Please add a comment")).inRoot(new ToastMatcher())
                .check(matches(withText("Please add a comment")));

        onView(withId(R.id.cancel_button_review)).perform(click());
    }

    @Test
    public void testFullSubmission() throws InterruptedException {
        navigateToRate();

        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRatingHelper.setRatingBar());
        onView(withId(R.id.editTextTextMultiLine)).perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("Success!")).inRoot(new ToastMatcher())
                .check(matches(withText("Success!")));

        Thread.sleep(2000);

        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));
        onView(withId(R.id.rate_button)).perform(click());
        onView(withId(R.id.rateFacilityView)).check(matches(isDisplayed()));

        onView(withId(R.id.ratingBar2)).perform(SetRatingHelper.setRatingBar());
        onView(withId(R.id.editTextTextMultiLine))
                .perform(typeText("Great overall experience!"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submit_button_review)).perform(click());

        onView(withText("You have reviewed in the past.")).inRoot(new ToastMatcher())
                .check(matches(withText("You have reviewed in the past.")));
        onView(withId(R.id.cancel_button_review)).perform(click());
        Thread.sleep(1500);

        onView(withId(R.id.facilityActivityView)).check(matches(isDisplayed()));

    }

}
