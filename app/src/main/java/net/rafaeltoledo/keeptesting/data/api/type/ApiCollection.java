package net.rafaeltoledo.keeptesting.data.api.type;

import java.util.List;

public class ApiCollection<T> {

    private final List<T> items;
    private final boolean hasMore;

    public ApiCollection(List<T> items, boolean hasMore) {
        this.items = items;
        this.hasMore = hasMore;
    }

    public List<T> getItems() {
        return items;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
