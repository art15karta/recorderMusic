package com.example.androidrecorder.Gallery

interface OnItemClickListener {

    fun onItemClickListener(position: Int)
    fun onItemLongClickListener(position: Int)
    fun editSongName(position: Int)
}