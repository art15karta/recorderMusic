package com.example.androidrecorder.Gallery

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidrecorder.Database.AudioRecord
import com.example.androidrecorder.R
import com.example.androidrecorder.SongProject.ProjectSongAdapter
import java.text.SimpleDateFormat
import java.util.Date

class GalleryAdapter(private val arrayAudio: List<AudioRecord>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {

        private var editMode = false

    fun isEditMode():Boolean {
        return editMode
    }

    fun setEditMode(mode: Boolean) {
        if (editMode != mode) {
            editMode = mode
            notifyDataSetChanged()
        }
    }

    inner class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        val name: TextView = itemView.findViewById(R.id.parentTitleTv)
        val imageView: ImageView = itemView.findViewById(R.id.parentLogoIv)
        val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val date: TextView = itemView.findViewById(R.id.date)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val songProjectRecyclerView = itemView.findViewById<RecyclerView>(R.id.childRecyclerView)
        val editBtn: ImageView = itemView.findViewById(R.id.btnEdit)
        val addSongBtn: ImageView = itemView.findViewById(R.id.btnRecordGallery)

        init {

            constraint.setOnClickListener(this)
            constraintLayout.setOnClickListener(this)
            name.setOnClickListener(this)
            imageView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClickListener(position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClickListener(position)
            }

            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.each_song_item, parent, false)
        return GalleryHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = arrayAudio[position]
            var dsf = SimpleDateFormat("hh:mm dd/MM/yy ")
            var date = Date(item.timeStamp)
            holder.name.text = item.filename
            holder.date.text = "Длительность: ${item.dutation} Создано: ${dsf.format(date)}"

            holder.songProjectRecyclerView.setHasFixedSize(true)
            holder.songProjectRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            val adapter = ProjectSongAdapter(arrayAudio)
            holder.songProjectRecyclerView.adapter = adapter

            holder.constraint.setOnClickListener {
                if (holder.songProjectRecyclerView.visibility == View.VISIBLE) {
                    holder.songProjectRecyclerView.visibility = View.GONE
                } else {
                    holder.songProjectRecyclerView.visibility = View.VISIBLE
                }

            }

            holder.constraintLayout.setOnClickListener {
                if (holder.songProjectRecyclerView.visibility == View.VISIBLE) {
                    holder.songProjectRecyclerView.visibility = View.GONE
                } else {
                    holder.songProjectRecyclerView.visibility = View.VISIBLE
                }
            }

            holder.name.setOnClickListener{
                if (holder.songProjectRecyclerView.visibility == View.VISIBLE) {
                    holder.songProjectRecyclerView.visibility = View.GONE
                } else {
                    holder.songProjectRecyclerView.visibility = View.VISIBLE
                }
            }

            holder.date.setOnClickListener{
                if (holder.songProjectRecyclerView.visibility == View.VISIBLE) {
                    holder.songProjectRecyclerView.visibility = View.GONE
                } else {
                    holder.songProjectRecyclerView.visibility = View.VISIBLE
                }
            }


            if (editMode) {
                holder.checkBox.visibility = View.VISIBLE
                holder.checkBox.isChecked = item.isCheked
            } else {
                holder.checkBox.visibility = View.GONE
                holder.checkBox.isChecked = false
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayAudio.size
    }


}