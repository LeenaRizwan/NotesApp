package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.NoteRepository

class NoteViewModelFactory(val app:Application, private val repos:NoteRepository): ViewModelProvider.Factory {
    override fun <T:ViewModel> create(modelClass: Class<T>): T{
        return NoteViewModel(app,repos) as T
    }
}