package net.rafaeltoledo.keeptesting.data.api;

import android.support.annotation.VisibleForTesting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class ApiCaller {

    private final static ApiCaller INSTANCE = new ApiCaller();
    private StackApi api = build();

    private StackApi build() {
        return build("https://api.stackexchange.com", null);
    }

    @VisibleForTesting
    public StackApi build(String apiHost, Interceptor idlingInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        if (idlingInterceptor != null) {
            clientBuilder.addInterceptor(idlingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create()
                ))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(StackApi.class);
    }

    @VisibleForTesting
    public void setApi(StackApi api) {
        this.api = api;
    }

    public static ApiCaller getInstance() {
        return INSTANCE;
    }

    private ApiCaller() {
        // This is a Singleton!
    }

    public StackApi getApi() {
        return api;
    }
}
