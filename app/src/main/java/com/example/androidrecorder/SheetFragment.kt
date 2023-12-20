package com.example.androidrecorder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidrecorder.ViewModel.RecorderViewModel
import com.example.androidrecorder.ViewModel.SaveDataClass
import com.example.androidrecorder.databinding.FragmentSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception

class SheetFragment : DialogFragment() {

    private lateinit var binding: FragmentSheetBinding
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var amplitudes: ArrayList<Float>
    private lateinit var old: String
    private lateinit var new: String
    private var dirPath = ""
    private var duration = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity()
        recorderViewModel = ViewModelProvider(activity).get(RecorderViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var bottomSb = BottomSheetBehavior.from(binding.standartBottomSheet)
//        bottomSb.apply {
//            peekHeight = 1000
//            state = BottomSheetBehavior.STATE_EXPANDED
//            isHideable
//            isFitToContents
//            this.halfExpandedRatio
//        }

        recorderViewModel.filenameDefault.observe(this) {
            binding.filenameInput.setText(it, TextView.BufferType.EDITABLE)
            old = it
        }

        recorderViewModel.filename.observe(this) {
            new = it
        }

        recorderViewModel.duration.observe(this) {
            duration = it
        }

        recorderViewModel.dirPath.observe(this) {
            dirPath = it
        }

        recorderViewModel.amplitudes.observe(this) {
            amplitudes = it
        }


        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnOkay.setOnClickListener {
            saveAction()
            val sD = SaveDataClass(requireContext())
            val audio = sD.saveFile(new, dirPath, amplitudes, duration)

            if (old != new) {
                sD.renameFile(new, old, dirPath)
            }
            recorderViewModel.setAudioRecord(audio)
        }

        binding.btnClear.setOnClickListener {
            binding.filenameInput.setText("")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun saveAction() {
        recorderViewModel.setData(binding.filenameInput.text.toString())
        Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT).show()
        dismiss()
    }



}