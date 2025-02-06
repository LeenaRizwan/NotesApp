package com.example.myapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.NoteDatabase
import com.example.myapplication.model.Note
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodel.NoteViewModelFactory
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {
    //Nav Drawer
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    lateinit var noteViewModel:NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
    }

    private fun setupViewModel(){
        val noteRepository=NoteRepository(NoteDatabase(this))
        val ViewModelProviderFactory=NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, ViewModelProviderFactory)[NoteViewModel::class.java]
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}