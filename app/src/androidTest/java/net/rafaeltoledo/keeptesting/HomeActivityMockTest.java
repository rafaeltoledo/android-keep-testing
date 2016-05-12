package net.rafaeltoledo.keeptesting;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.rafaeltoledo.keeptesting.data.api.StackApi;
import net.rafaeltoledo.keeptesting.data.api.type.ApiCollection;
import net.rafaeltoledo.keeptesting.data.model.User;
import net.rafaeltoledo.keeptesting.espresso.TestUtils;
import net.rafaeltoledo.keeptesting.ui.activity.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.Charset;

import okio.BufferedSource;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomeActivityMockTest implements IdlingResource {

    @Rule
    public ActivityTestRule<HomeActivity> activityRule =
            new ActivityTestRule<>(HomeActivity.class, false, false);

    @Mock
    private StackApi mockApi;

    private ResourceCallback callback;

    @Before
    public void setupMockServerAndIdlingResource() {
        registerIdlingResources(this);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void releaseIdlingResource() {
        unregisterIdlingResources(this);
    }

    @Test
    public void shouldLoadUsersAndTriggerIntent() {
        when(mockApi.getUsers(anyInt())).thenReturn(successResult());

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

    private Observable<Response<ApiCollection<User>>> successResult() {
        return Observable.just(Response.success(fromFile("users.json")));
    }

    protected ApiCollection<User> fromFile(String file) {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(readFile(file), new TypeToken<ApiCollection<User>>() {}.getType());
    }

    private String readFile(String location) {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        try {
            BufferedSource source = Okio.buffer(Okio.source(instrumentation.getContext().getAssets()
                    .open(String.format("fixtures/%s", location))));
            return source.readString(Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName() {
        return "HomeActivityMockTest";
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = false;
        HomeActivity activity = activityRule.getActivity();
        if (activity == null) {
            return false;
        }

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            idle = recyclerView.getAdapter().getItemCount() > 1;
        }

        if (idle && callback != null) {
            callback.onTransitionToIdle();
        }

        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
