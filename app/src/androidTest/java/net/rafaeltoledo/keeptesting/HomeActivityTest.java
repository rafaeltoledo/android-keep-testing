package net.rafaeltoledo.keeptesting;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.rafaeltoledo.keeptesting.data.api.ApiCaller;
import net.rafaeltoledo.keeptesting.espresso.OkHttpIdlingResource;
import net.rafaeltoledo.keeptesting.espresso.TestUtils;
import net.rafaeltoledo.keeptesting.ui.activity.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityRule =
            new ActivityTestRule<>(HomeActivity.class, false, false);

    @Rule
    public InstrumentedTestRequestMatcherRule serverRule =
            new InstrumentedTestRequestMatcherRule(InstrumentationRegistry.getContext());

    OkHttpIdlingResource idlingResource;

    @Before
    public void setupMockServerAndIdlingResource() {
        String url = serverRule.url("/").toString();
        idlingResource = new OkHttpIdlingResource(url);
        ApiCaller.getInstance().setApi(ApiCaller.getInstance().build(url, idlingResource));
        registerIdlingResources(idlingResource);
    }

    @After
    public void releaseIdlingResource() {
        unregisterIdlingResources(idlingResource);
    }

    @Test
    public void shouldLoadUsersAndTriggerIntent() {
        serverRule
                .enqueueGET(200, "users.json")
                .assertPathIs("/2.2/users")
                .assertHasQuery("order", "desc")
                .assertHasQuery("sort", "reputation")
                .assertHasQuery("site", "stackoverflow");

        Intents.init();
        activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        onView(withId(R.id.recyclerView))
                .perform(scrollToPosition(3));

        TestUtils.rotateScreen(activityRule);

        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition(2, click()));

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("http://stackoverflow.com/users/157882/balusc"))
        ));
        Intents.release();
    }
}
