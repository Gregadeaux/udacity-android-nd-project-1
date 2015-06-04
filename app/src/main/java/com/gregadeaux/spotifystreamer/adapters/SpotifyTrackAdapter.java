package com.gregadeaux.spotifystreamer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.databinding.CellTrackBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by greg on 6/3/15.
 */
public class SpotifyTrackAdapter extends RecyclerView.Adapter<SpotifyTrackAdapter.ViewHolder> {

    private List<Track> trackList;
    private Context context;

    public SpotifyTrackAdapter(Context context) {
        this.context = context;
        this.trackList = new ArrayList<>();
    }

    public boolean setTracks(List<Track> trackList) {
        this.trackList.clear();
        return this.trackList.addAll(trackList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                    .inflate(R.layout.cell_track, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.binding.setTrack(track);

        if(track.album.images.size() > 0) {
            Picasso.with(context)
                    .load(track.album.images.get(0).url)
                    .resize(150,150)
                    .centerCrop()
                    .into(holder.binding.cellTrackImage);
        }
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CellTrackBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = CellTrackBinding.bind(itemView);
        }
    }
}
