package com.iwmh.spotifywearapp.fragments.recyclerviewadapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iwmh.spotifywearapp.R
import com.iwmh.spotifywearapp.models.WebAPI.Playlist
import com.iwmh.spotifywearapp.utils.Constants
import com.iwmh.spotifywearapp.viewmodels.PlaylistsViewModel


class PlaylistsRecyclerViewAdapter(
    private val context: Context,
    private val playlistsViewModel: PlaylistsViewModel
) : RecyclerView.Adapter<PlaylistsRecyclerViewAdapter.ViewHolder>(){

    private var playlists = listOf<Playlist>()

    fun setDataSet(newLists: List<Playlist>){
        playlists = newLists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // set playlist image
        val item = playlists[position]
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
            playlists.forEachIndexed { index, playlist ->
                playlist.currentlyPlaying = index == position
            }

            playlistsViewModel.listOfPlaylists.postValue(playlists)
        }
    }

    override fun getItemCount(): Int = playlists.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistImage: ImageView = view.findViewById(R.id.playlist_image)
        val playlistName: TextView = view.findViewById(R.id.playlist_name)
    }

}