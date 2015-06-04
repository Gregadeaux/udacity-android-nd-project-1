package com.gregadeaux.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by greg on 6/3/15.
 */
public class ParcelableTrack implements Parcelable {

    public String name;
    public String album;
    public String imageUrl;

    public ParcelableTrack(String name, String album, String imageUrl) {
        this.name = name;
        this.album = album;
        this.imageUrl = imageUrl;
    }

    private ParcelableTrack(Parcel in) {
        name = in.readString();
        album = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ParcelableTrack> CREATOR = new Creator<ParcelableTrack>() {
        @Override
        public ParcelableTrack createFromParcel(Parcel in) {
            return new ParcelableTrack(in);
        }

        @Override
        public ParcelableTrack[] newArray(int size) {
            return new ParcelableTrack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(album);
        dest.writeString(imageUrl);
    }
}
