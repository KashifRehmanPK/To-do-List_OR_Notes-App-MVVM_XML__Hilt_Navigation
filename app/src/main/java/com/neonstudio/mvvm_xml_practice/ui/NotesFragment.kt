package com.neonstudio.mvvm_xml_practice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.neonstudio.mvvm_xml_practice.R
import com.neonstudio.mvvm_xml_practice.databinding.FragmentLoginBinding
import com.neonstudio.mvvm_xml_practice.databinding.FragmentNotesBinding
import com.neonstudio.mvvm_xml_practice.models.NoteRequest
import com.neonstudio.mvvm_xml_practice.models.NoteResponse
import com.neonstudio.mvvm_xml_practice.utils.NetworkResult
import com.neonstudio.mvvm_xml_practice.viewmodels.NoteViewModel


class NotesFragment : Fragment() {


    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note: NoteResponse? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_notes, container, false)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandler()
        bindObesrver()

    }

    private fun bindObesrver() {
noteViewModel.statusLiveData.observe(viewLifecycleOwner,{
    when (it){
        is NetworkResult.Success -> {
            findNavController().popBackStack()
        }
        is NetworkResult.Error -> {}
        is NetworkResult.Loading -> {}

    }
})
    }

    private fun bindHandler() {
        binding.btnDelete.setOnClickListener() {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }


        binding.btnSubmit.setOnClickListener() {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(description, title)
            if (noteRequest == null) {
                noteViewModel.createNote(noteRequest)
            } else {
                noteViewModel.updateNotes(note!!._id, noteRequest)
            }
        }

    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        } else {
            binding.addEditText.text = "Add Note"
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}