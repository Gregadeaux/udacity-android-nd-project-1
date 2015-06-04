package com.gregadeaux.spotifystreamer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.adapters.SpotifyTrackAdapter;
import com.gregadeaux.spotifystreamer.databinding.ActivityArtistBinding;
import com.gregadeaux.spotifystreamer.models.ParcelableTrack;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class ArtistActivity extends AppCompatActivity {

    private static final String TRACK_EXTRA_TAG = "tracks";

    private RecyclerView mRecyclerView;
    private SpotifyTrackAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SpotifyApi api;
    private String artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityArtistBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_artist);
        mRecyclerView = binding.activityArtistList;

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SpotifyTrackAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        artist = getIntent().getStringExtra(SearchActivity.ARTIST_EXTRA_TAG);

        api = new SpotifyApi();
        if(savedInstanceState != null) {
            ParcelableTrack[] pTracks = (ParcelableTrack[]) savedInstanceState.getParcelableArray(TRACK_EXTRA_TAG);
            int size = pTracks.length;

            List<Track> artists = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                artists.add(pTracks[i].toTrack());
            }

            mAdapter.setTracks(artists);
            mAdapter.notifyDataSetChanged();
        }else {
            new TrackAsyncTask(api, mAdapter, this).execute(artist);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        List<Track> tracks = mAdapter.getTracks();
        int size = tracks.size();

        ParcelableTrack[] pTracks = new ParcelableTrack[size];
        for(int i = 0; i < size; i++) {
            pTracks[i] = ParcelableTrack.fromTrack(tracks.get(i));
        }

        savedInstanceState.putParcelableArray(TRACK_EXTRA_TAG, pTracks);
    }

    private static class TrackAsyncTask extends AsyncTask<String, ObjectUtils.Null, Tracks> implements DialogInterface.OnKeyListener {

        private static Map<String, Object> queryOptions = new ArrayMap<>();
        static { queryOptions.put("country", "us"); }

        private SpotifyApi api;
        private SpotifyTrackAdapter adapter;
        private Context context;
        private ProgressDialog dialog;

        public TrackAsyncTask(SpotifyApi api, SpotifyTrackAdapter adapter, Context context) {
            this.api = api;
            this.adapter = adapter;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "", "Loading Tracks", true);
            dialog.setOnKeyListener(this);
        }

        @Override
        protected Tracks doInBackground(String... params) {
            if(params.length > 0) {
                return api.getService().getArtistTopTrack(params[0], queryOptions);
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Tracks result) {
            dialog.hide();
            if(result == null || result.tracks.size() == 0) {
                Toast.makeText(context, "No results were found", Toast.LENGTH_LONG).show();
            }else {
                adapter.setTracks(result.tracks);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
            }
            return false;
        }
    }
}
