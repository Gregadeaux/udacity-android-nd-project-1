package com.gregadeaux.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

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

    public Track toTrack() {
        Track temp  = new Track();
        Image tempImage = new Image();

        temp.name = this.name;
        temp.album = new Album();
        temp.album.name = this.album;
        temp.album.images = new ArrayList<>();
        tempImage.url = this.imageUrl;
        temp.album.images.add(tempImage);

        return temp;
    }

    public static ParcelableTrack fromTrack(Track track) {
        return new ParcelableTrack(track.name, track.album.name, track.album.images.size() > 0 ? track.album.images.get(0).url : "");
    }
}
