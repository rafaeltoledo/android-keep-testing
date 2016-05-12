package net.rafaeltoledo.keeptesting.espresso;

import android.support.test.espresso.IdlingResource;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class OkHttpIdlingResource implements IdlingResource, Interceptor {

    private final String url;
    private IdlingResource.ResourceCallback callback;
    private boolean busy = false;

    public OkHttpIdlingResource(String url) {
        this.url = url;
    }

    @Override
    public boolean isIdleNow() {
        if (!busy && callback != null) {
            callback.onTransitionToIdle();
        }
        return !busy;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    @Override
    public String getName() {
        return "net.rafaeltoledo.stak.espresso.ApiCallerIdlingResource";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain.request().url().toString().startsWith(url)) {
            busy = true;
            Response response = chain.proceed(chain.request());
            busy = false;
            return response;
        } else {
            return chain.proceed(chain.request());
        }
    }
}
