package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Note
import com.example.myapplication.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(app: Application, private val repos:NoteRepository):AndroidViewModel(app) {

    fun AddNote(note: Note){
        viewModelScope.launch {
            repos.insertNote(note)
        }
    }

    fun DelNote(note: Note){
        viewModelScope.launch {
            repos.deleteNote(note)
        }
    }

    fun UpdateNote(note: Note){
        viewModelScope.launch {
            repos.updateNote(note)
        }
    }

    fun getAllNotes()=repos.getAllNotes()

    fun searchNote(query:String?)=repos.searchNotes(query)
}