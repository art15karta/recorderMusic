package com.example.androidrecorder.Gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.androidrecorder.AudioPlayerActivity
import com.example.androidrecorder.Database.AppDataBase
import com.example.androidrecorder.Database.AudioRecord
import com.example.androidrecorder.databinding.ActivityGalleryBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class GalleryActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var records: ArrayList<AudioRecord>
    private lateinit var mAdapter: GalleryAdapter
    private lateinit var oneSong: AudioRecord
    private lateinit var dataBase: AppDataBase
    private lateinit var searchBtn: TextInputEditText
    private lateinit var btnClearText: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        records = ArrayList()
        dataBase = Room.databaseBuilder(this, AppDataBase::class.java, "audioRecord").build()

        mAdapter = GalleryAdapter(records, this)

        binding.recyclerViewParent.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

        }

        btnClearText = binding.clearTextBtn

        btnClearText.setOnClickListener {
            searchBtn.setText("", TextView.BufferType.EDITABLE)
        }

        searchBtn = binding.searchBtn
        searchBtn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var query = p0.toString()
                searchDatabase(query)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        fetchAll()

    }

    private fun searchDatabase(query: String) {
        GlobalScope.launch {
            records.clear()
            var queryResult = dataBase.AppDataBaseDao().searchDatabase("%$query%")
            records.addAll(queryResult)

            runOnUiThread{
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun fetchAll() {
        GlobalScope.launch {
            records.clear()
            var queryResult = dataBase.AppDataBaseDao().getAll()
            records.addAll(queryResult)
            mAdapter.notifyDataSetChanged()

        }
    }

    override fun onItemClickListener(position: Int) {
        var audioRecord = records[position]

//        if (mAdapter.isEditMode()) {
//            mAdapter.setEditMode(true)
//            records[position].isCheked
//            mAdapter.notifyItemChanged(position)
//        } else {
//            val intent = Intent(this@GalleryActivity, AudioPlayerActivity::class.java)
//            Log.d("MAIN_REC", "Audiorecord: $audioRecord\n")
//            try {
//                intent.putExtra("filepath", audioRecord.filepath)
//                intent.putExtra("filename", audioRecord.filename)
//            } catch (e: Exception) {
//                println(e)
//            }
//
//            startActivity(intent)
//        }

    }

    override fun onItemLongClickListener(position: Int) {
        mAdapter.setEditMode(true)
        records[position].isCheked
        mAdapter.notifyItemChanged(position)
    }

    override fun editSongName(position: Int) {
        var audioRecord = records[position]

    }

}