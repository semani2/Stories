package com.se.stories

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.linkedin.android.testbutler.TestButler
import com.se.stories.activity.StoriesActivity
import com.se.stories.recycleview.RecyclerViewItemCountAssertion
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class StoriesActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(StoriesActivity::class.java, false, false)

    /**
     * Tests the default start state of the application
     */
    @Test
    fun `test_start_state`() {
        activityRule.launchActivity(null)

        Espresso.onView(withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Tests that all stories are successfully fetched
     */
    @Test
    fun `test_all_stories_fetched`() {
        activityRule.launchActivity(null)

        val idlingResource = activityRule.activity.countingIdlingResource

        IdlingRegistry.getInstance().register(idlingResource)

        Espresso.onView(withId(R.id.stories_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.stories_recycler_view)).check(
            RecyclerViewItemCountAssertion(10)
        )
    }

    /**
     * Tests that the stories are persisted when there is no network connection
     * after the initial fetch of data when there is connection
     */
    @Test
    fun `test_no_network_connection_items_fetched`() {
        activityRule.launchActivity(null)

        val idlingResource = activityRule.activity.countingIdlingResource

        IdlingRegistry.getInstance().register(idlingResource)

        Espresso.onView(withId(R.id.stories_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.stories_recycler_view)).check(
            RecyclerViewItemCountAssertion(10)
        )

        toggleConnectivity(false)

        Thread.sleep(1000)

        onView(withId(R.id.swipe_refresh_layout))
            .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        Espresso.onView(withId(R.id.stories_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.stories_recycler_view)).check(
            RecyclerViewItemCountAssertion(10)
        )
    }

    @After
    fun teardown() {
        toggleConnectivity(true)
        IdlingRegistry.getInstance().unregister(activityRule.activity.countingIdlingResource)
    }

    private fun toggleConnectivity(enable: Boolean) {
        TestButler.setGsmState(enable)
        TestButler.setWifiState(enable)
    }

    /**
     * Source: https://stackoverflow.com/questions/33505953/espresso-how-to-test-swiperefreshlayout
     */
    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }
}
