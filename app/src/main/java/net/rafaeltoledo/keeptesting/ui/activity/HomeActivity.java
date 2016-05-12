package net.rafaeltoledo.keeptesting.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.rafaeltoledo.keeptesting.R;
import net.rafaeltoledo.keeptesting.data.api.ApiCaller;
import net.rafaeltoledo.keeptesting.data.api.type.ApiCollection;
import net.rafaeltoledo.keeptesting.data.model.User;
import net.rafaeltoledo.keeptesting.ui.adapter.UserAdapter;
import net.rafaeltoledo.keeptesting.ui.listener.OnLoadMoreListener;
import net.rafaeltoledo.keeptesting.ui.listener.OnUserClickListener;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class HomeActivity extends AppCompatActivity implements OnLoadMoreListener, OnUserClickListener,
        Observer<Response<ApiCollection<User>>> {

    private RecyclerView.LayoutManager layoutManager;
    private UserAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        adapter = new UserAdapter(this, this);

        if (savedInstanceState != null) {
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("lm"));
            adapter.onRestoreInstanceState(savedInstanceState.getParcelable("adapter"));
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("lm", layoutManager.onSaveInstanceState());
        outState.putParcelable("adapter", adapter.onSaveInstanceState());
    }

    @Override
    public void loadMore(int page) {
        ApiCaller.getInstance().getApi().getUsers(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(user.getLink()));
        startActivity(intent);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Response<ApiCollection<User>> response) {
        if (response.isSuccessful()) {
            adapter.addAll(response.body().getItems(), response.body().isHasMore());
        }
    }

    @Override
    public void onCompleted() {
        // Nothing to do!
    }
}
