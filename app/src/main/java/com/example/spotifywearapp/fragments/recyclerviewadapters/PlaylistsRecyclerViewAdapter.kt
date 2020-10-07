package com.example.spotifywearapp.fragments.recyclerviewadapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifywearapp.R
import com.example.spotifywearapp.models.WebAPI.Playlist
import com.example.spotifywearapp.utils.Constants
import com.example.spotifywearapp.viewmodels.PlaylistsViewModel


class PlaylistsRecyclerViewAdapter(
    private val values: List<Playlist>,
    private val context: Context,
    private val playlistsViewModel: PlaylistsViewModel
) : RecyclerView.Adapter<PlaylistsRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // set playlist image
        val item = values[position]
        Glide.with(context).load(item.images[0].url).into(holder.playlistImage)
        // set playlist name
        holder.playlistName.text = item.name

        // if it is currently playing, change its color
        if(item.currentlyPlaying){
            holder.playlistName.setTextColor(Color.GREEN)
        } else {
            holder.playlistName.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener{ v ->
            // play clicked playlist's track
            playlistsViewModel.playPlaylist(context, item.uri)

            // register playlist id to sharedpref
            playlistsViewModel.storeDataToStorage(context, Constants.currently_playing_playlist_id, item.id)

            // update the currently-playing status
            values.forEachIndexed { index, playlist ->
                playlist.currentlyPlaying = index == position
            }

            playlistsViewModel.listOfPlaylists.postValue(values)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistImage: ImageView = view.findViewById(R.id.playlist_image)
        val playlistName: TextView = view.findViewById(R.id.playlist_name)
    }

}