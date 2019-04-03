package com.example.android.nitkart;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SellActivityTest {



    @Rule
    public ActivityTestRule<SellActivity> mActivityTestRule = new ActivityTestRule<SellActivity>(SellActivity.class);

    private String sellerName = "XYZ";


    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void testUserInputScenario()
    {
        // input some text in the edit text
        Espresso.onView(withId(R.id.seller_name)).perform(typeText(sellerName));
        // close soft keyboard
        Espresso.closeSoftKeyboard();
        // perform button click
//        Espresso.onView(withId(R.id.btnChange)).perform(click());
//        // checking the text in the textview
//        Espresso.onView(withId(R.id.tvChangedText)).check(matches(withText(mName)));
    }


    @After
    public void tearDown() throws Exception {
    }
}