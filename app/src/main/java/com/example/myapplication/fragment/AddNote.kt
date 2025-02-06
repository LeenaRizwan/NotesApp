package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNoteBinding
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.NoteViewModel

//The job here is to save title and note contents in the database

class AddNote : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addNoteBinding : FragmentAddNoteBinding?= null
    private val binding get()=addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding=FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding. root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialise menuhost
        val menuHost: MenuHost =requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        //initialise viewmodel
        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view
    }

    private fun saveNote(view:View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if(noteTitle.isNotEmpty()){
            //saving into database!
            val note = Note(0,noteTitle,noteDesc)
            notesViewModel.AddNote(note)

            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }
        else{
            val note = Note(0,"Untitled",noteDesc)
            notesViewModel.AddNote(note)

            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    //do this for onCreateMenu everytime ig
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    //if save successful then true, else false
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        addNoteBinding=null
    }
}