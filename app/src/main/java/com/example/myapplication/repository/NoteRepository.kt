package com.example.myapplication.repository

import com.example.myapplication.database.NoteDatabase
import com.example.myapplication.model.Note

class NoteRepository(private val db:NoteDatabase) {
    //functions to call the methods in dao
    //So to recap, we set up the table in Note.kt, in model
    // //@Entity tablename, parcelised it and made a @Primaykey that was autogen
    //Next in the database package,
    // // @dao file, we put in all the queries we'd need, @Insert, Update, Delete, then 2 @Query to findall and search
    // //Then we also make the database. That part's still a bit foggy to me,
    // // but we call RoomDatabase Builder for the job, and call entity Notes as our table
    //And NOW, in repos, we make functions to call the DAO functions

    //suspend makes sure it runs in the background?
    suspend fun insertNote(note: Note)=db.getNoteDao().insertNote(note)
    suspend fun updateNote(note:Note)=db.getNoteDao().updateNote(note)
    suspend fun deleteNote(note:Note)=db.getNoteDao().deleteNote(note)

    fun getAllNotes()=db.getNoteDao().getAllNotes()
    fun searchNotes(query: String?)=db.getNoteDao().searchNote(query)
}