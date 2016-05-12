package net.rafaeltoledo.keeptesting.ui.adapter;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.rafaeltoledo.keeptesting.R;
import net.rafaeltoledo.keeptesting.data.model.User;
import net.rafaeltoledo.keeptesting.ui.holder.LoadingViewHolder;
import net.rafaeltoledo.keeptesting.ui.holder.UserViewHolder;
import net.rafaeltoledo.keeptesting.ui.listener.OnLoadMoreListener;
import net.rafaeltoledo.keeptesting.ui.listener.OnUserClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final OnUserClickListener userlistener;
    private final OnLoadMoreListener loadMoreListener;

    private int page = 0;
    private List<User> items = new ArrayList<>();
    private boolean dontNotify = false;

    public UserAdapter(OnUserClickListener userlistener, OnLoadMoreListener loadMoreListener) {
        this.userlistener = userlistener;
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
        }
        return new LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(items.get(position), userlistener);
        } else {
            if (!dontNotify) loadMoreListener.loadMore(++page);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (dontNotify || position != getItemCount() - 1) ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return items.size() + (dontNotify ? 0 : 1);
    }

    public void addAll(List<User> items, boolean continueLoading) {
        this.items.addAll(items);
        dontNotify = !continueLoading;
        notifyDataSetChanged();
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            page = savedState.getPage();
            items = new ArrayList<>(Arrays.asList((User[]) savedState.getItems()));
        }
    }

    public Parcelable onSaveInstanceState() {
        return new SavedState<>(page, items.toArray(new User[items.size()]));
    }

    public List<User> getItems() {
        return Collections.unmodifiableList(items);
    }
}
