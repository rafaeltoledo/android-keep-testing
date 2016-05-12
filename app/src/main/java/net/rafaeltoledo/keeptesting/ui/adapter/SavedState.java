package net.rafaeltoledo.keeptesting.ui.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class SavedState<T extends Parcelable> implements Parcelable {

    private final int page;
    private final T[] items;

    public SavedState(Parcel parcel) {
        this.page = parcel.readInt();
        this.items = (T[]) parcel.readParcelableArray(ClassLoader.getSystemClassLoader());
    }

    public SavedState(int page, T[] items) {
        this.page = page;
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public T[] getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeParcelableArray(items, 0);
    }

    public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
        @Override
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        @Override
        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}
