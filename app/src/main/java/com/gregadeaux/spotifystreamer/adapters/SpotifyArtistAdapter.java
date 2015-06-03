package com.gregadeaux.spotifystreamer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregadeaux.spotifystreamer.R;
import com.gregadeaux.spotifystreamer.databinding.CellArtistBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by greg on 6/3/15.
 */
public class SpotifyArtistAdapter extends RecyclerView.Adapter<SpotifyArtistAdapter.ViewHolder>{

    private List<Artist> artists;
    private Context context;
    private SpotifyAdapterClickListener listener;

    public SpotifyArtistAdapter(Context context) {
        this.context = context;
        this.artists = new ArrayList<>();
    }

    public void setClickListener(SpotifyAdapterClickListener listener) {
        this.listener = listener;
    }

    public boolean setArtists(List<Artist> artists) {
        this.artists.clear();
        return this.artists.addAll(artists);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_artist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Artist artist = artists.get(position);

        holder.binding.setArtist(artist);
        holder.binding.cellArtistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.artistClicked(artist);
                }
            }
        });

        if(artist.images.size() > 0) {
            Picasso.with(context)
                    .load(artist.images.get(0).url)
                    .resize(150,150)
                    .centerCrop()
                    .into(holder.binding.cellArtistImage);
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CellArtistBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = CellArtistBinding.bind(itemView);
        }
    }

    public interface SpotifyAdapterClickListener {
        public void artistClicked(Artist artist);
    }
}
