package com.neonstudio.mvvm_xml_practice.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonstudio.mvvm_xml_practice.models.NoteRequest
import com.neonstudio.mvvm_xml_practice.repository_db.NoteRepository
import com.neonstudio.mvvm_xml_practice.repository_db.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NoteRepository): ViewModel() {

    val noteLiveData get() = notesRepository.notesLiveData
    val statusLiveData get() = notesRepository.statusLiveData

    fun getNotes(){
        viewModelScope.launch {
         notesRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.createNote(noteRequest)
        }
    }

    fun updateNotes(noteId: String, noteRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.updateNote(noteId,noteRequest)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            notesRepository.deleteNotes(noteId)
        }
    }

}