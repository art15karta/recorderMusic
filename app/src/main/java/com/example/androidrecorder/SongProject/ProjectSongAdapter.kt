package com.example.androidrecorder.SongProject


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidrecorder.Database.AudioRecord
import com.example.androidrecorder.R

class ProjectSongAdapter(private val audioList: List<AudioRecord>):  RecyclerView.Adapter<ProjectSongAdapter.ProjectSongHolder>() {

    inner class ProjectSongHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val btnPlay: ImageView = itemView.findViewById(R.id.idPlay)
        val songName: TextView = itemView.findViewById(R.id.songName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectSongHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_song_item, parent, false)
        return ProjectSongHolder(view)
    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    override fun onBindViewHolder(holder: ProjectSongHolder, position: Int) {
        val oneSongItem = audioList[position]

        holder.songName.text = oneSongItem.filename

        holder.btnPlay.setOnClickListener {


        }
        }


    }


