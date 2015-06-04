package com.gregadeaux.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by greg on 6/3/15.
 */
public class ParcelableArtist implements Parcelable {

    public String name;
    public String id;
    public String imageUrl;

    public ParcelableArtist(String name, String id, String imageUrl) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    private ParcelableArtist(Parcel in) {
        name = in.readString();
        id = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ParcelableArtist> CREATOR = new Creator<ParcelableArtist>() {
        @Override
        public ParcelableArtist createFromParcel(Parcel in) {
            return new ParcelableArtist(in);
        }

        @Override
        public ParcelableArtist[] newArray(int size) {
            return new ParcelableArtist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(imageUrl);
    }

    public Artist toArtist() {
        Artist temp  = new Artist();
        Image tempImage = new Image();
        temp.name = this.name;
        temp.id = this.id;
        temp.images = new ArrayList<>();
        tempImage.url = this.imageUrl;
        temp.images.add(tempImage);
        return temp;
    }

    public static ParcelableArtist fromArtist(Artist artist) {
        return new ParcelableArtist(artist.name, artist.id, artist.images.size() > 0 ? artist.images.get(0).url : "");
    }
}
