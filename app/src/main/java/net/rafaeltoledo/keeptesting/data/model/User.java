package net.rafaeltoledo.keeptesting.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private final String displayName;
    private final String profileImage;
    private final String location;
    private final String link;

    public User(String displayName, String profileImage, String location, String link) {
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.location = location;
        this.link = link;
    }

    protected User(Parcel in) {
        displayName = in.readString();
        profileImage = in.readString();
        location = in.readString();
        link = in.readString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(profileImage);
        dest.writeString(location);
        dest.writeString(link);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
