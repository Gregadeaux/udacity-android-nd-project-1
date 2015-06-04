package com.gregadeaux.spotifystreamer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.adapters.SpotifyArtistAdapter;
import com.gregadeaux.spotifystreamer.databinding.ActivitySearchBinding;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SearchActivity extends AppCompatActivity implements TextWatcher, SpotifyArtistAdapter.SpotifyAdapterClickListener {

    public static final String ARTIST_EXTRA_TAG = "artist";
    private static final long TEXT_DELAY = 1000;

    private EditText searchField;
    private RecyclerView mRecyclerView;
    private SpotifyArtistAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Timer textWaitTimer;
    private TimerTask textWaitTimerTask;
    private SpotifyApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        searchField = binding.activitySearchField;
        mRecyclerView = binding.activitySearchList;

        searchField.addTextChangedListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SpotifyArtistAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        textWaitTimer = new Timer();
        api = new SpotifyApi();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        textWaitTimer.cancel();
        textWaitTimer.purge();
        textWaitTimer = new Timer();
        textWaitTimerTask = new SpotifySearchTimerTask();
        textWaitTimer.schedule(textWaitTimerTask, TEXT_DELAY);
    }

    @Override
    public void artistClicked(Artist artist) {
        Intent theIntent = new Intent(this, ArtistActivity.class);
        theIntent.putExtra(ARTIST_EXTRA_TAG, artist.id);
        startActivity(theIntent);
    }

    private class SpotifySearchTimerTask extends TimerTask {
        @Override
        public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new SpotifySearchAsyncTask(api, mAdapter, SearchActivity.this)
                            .execute(searchField.getText().toString());
                }
            });
        }
    }

    private class SpotifySearchAsyncTask extends AsyncTask<String, ObjectUtils.Null, ArtistsPager> implements DialogInterface.OnKeyListener {

        private SpotifyApi api;
        private SpotifyArtistAdapter adapter;
        private Context context;
        private ProgressDialog dialog;

        public SpotifySearchAsyncTask(SpotifyApi api, SpotifyArtistAdapter adapter, Context context) {
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
        protected ArtistsPager doInBackground(String... params) {
            if(params.length > 0) {
                return api.getService().searchArtists(params[0]);
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager result) {
            dialog.hide();

            if(result == null || result.artists.items.size() == 0) {
                Toast.makeText(context, "No results were found", Toast.LENGTH_LONG).show();
            }else {
                adapter.setArtists(result.artists.items);
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
