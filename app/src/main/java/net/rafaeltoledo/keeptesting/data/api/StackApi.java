package net.rafaeltoledo.keeptesting.data.api;

import net.rafaeltoledo.keeptesting.data.api.type.ApiCollection;
import net.rafaeltoledo.keeptesting.data.model.User;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface StackApi {

    @GET("2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Observable<Response<ApiCollection<User>>> getUsers(@Query("page") int page);
}
