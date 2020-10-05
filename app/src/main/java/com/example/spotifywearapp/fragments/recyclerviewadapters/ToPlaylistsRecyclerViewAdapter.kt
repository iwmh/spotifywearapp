package com.example.spotifywearapp.fragments.recyclerviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifywearapp.R
import com.example.spotifywearapp.models.WebAPI.Playlist
import com.example.spotifywearapp.viewmodels.PlaylistsViewModel


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ToPlaylistsRecyclerViewAdapter(
    private val values: List<Playlist>,
    private val context: Context,
    private val playlistsViewModel: PlaylistsViewModel
) : RecyclerView.Adapter<ToPlaylistsRecyclerViewAdapter.ViewHolder>(){

    private var mRecycler: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecycler = null
    }

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

//        holder.itemView.setOnClickListener{ v ->
//            playlistsViewModel.playPlaylist(context, item.uri)
//        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistImage: ImageView = view.findViewById(R.id.playlist_image)
        val playlistName: TextView = view.findViewById(R.id.playlist_name)
    }

}