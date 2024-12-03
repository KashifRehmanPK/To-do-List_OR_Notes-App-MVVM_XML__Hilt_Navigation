package com.neonstudio.mvvm_xml_practice.ui

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.neonstudio.mvvm_xml_practice.R
import com.neonstudio.mvvm_xml_practice.api.NotesAPI
import com.neonstudio.mvvm_xml_practice.databinding.FragmentLoginBinding
import com.neonstudio.mvvm_xml_practice.databinding.FragmentMainBinding
import com.neonstudio.mvvm_xml_practice.models.NoteResponse
import com.neonstudio.mvvm_xml_practice.ui.adapters.NoteAdapter
import com.neonstudio.mvvm_xml_practice.utils.NetworkResult
import com.neonstudio.mvvm_xml_practice.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    //    @Inject
//    lateinit var notesAPI: NotesAPI
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()

    private lateinit var adapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        CoroutineScope(Dispatchers.IO).launch {
//            val result= notesAPI.getNotes()
//        }

        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        adapter = NoteAdapter(::onNoteClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        noteViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener(){
            findNavController().navigate(R.id.action_mainFragment_to_notesFragment)
        }
    }




    private fun bindObservers() {
        noteViewModel.noteLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun onNoteClicked(noteResponse: NoteResponse){
        val bundle = Bundle()
        bundle.putString("note",Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_notesFragment,bundle)
        //Toast.makeText(requireContext(), "note - ${noteResponse.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
