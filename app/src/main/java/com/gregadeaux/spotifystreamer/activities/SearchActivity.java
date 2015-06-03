package com.gregadeaux.spotifystreamer.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.adapters.SpotifyArtistAdapter;
import com.gregadeaux.spotifystreamer.databinding.ActivitySearchBinding;

import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity implements TextWatcher, SpotifyArtistAdapter.SpotifyAdapterClickListener {

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

    }

    private class SpotifySearchTimerTask extends TimerTask {
        @Override
        public void run() {
            api.getService().searchArtists(searchField.getText().toString(), new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    if(artistsPager.artists.items.size() > 0) {
                        mAdapter.setArtists(artistsPager.artists.items);
                        SearchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else {
                        Toast.makeText(SearchActivity.this, "No results found", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SearchActivity.this, "An error occurred: " + error.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            });
        }
    }
}
