package com.gregadeaux.spotifystreamer.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.adapters.SpotifyArtistAdapter;
import com.gregadeaux.spotifystreamer.databinding.ActivityArtistBinding;

public class ArtistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityArtistBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_artist);
        mRecyclerView = binding.activityArtistList;

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SpotifyArtistAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
