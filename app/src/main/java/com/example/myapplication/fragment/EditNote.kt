package com.example.myapplication.fragment

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEditNoteBinding
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.NoteViewModel

//Two tasks here, delete old note, and update with new changes

class EditNote : Fragment(R.layout.fragment_edit_note), MenuProvider {

    private var editNoteBinding: FragmentEditNoteBinding?= null
    private val binding get()= editNoteBinding!!

    private lateinit var notesViewModel : NoteViewModel
    private lateinit var currentNote:Note

    private val args: EditNoteArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote=args.note!!

        //save new changes when button clicked
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        binding.editNoteFab.setOnClickListener{
            val noteTitle=binding.editNoteTitle.text.toString().trim()
            val notedesc=binding.editNoteDesc.text.toString().trim()

            if(noteTitle.isNotEmpty()){
                val note=Note(currentNote.id, noteTitle,notedesc)
                notesViewModel.UpdateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
            else{
                val note=Note(currentNote.id, "Untitled",notedesc)
                notesViewModel.UpdateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    //Delete feature idher implement karna hai
    //Basically make an alert dialog, wooooaw grape
    private fun deleteNote(){
        AlertDialog.Builder(activity).apply{
            setTitle("Delete Note")
            setMessage("Note will be Permentantly Deleted. Are you Sure?")
            setPositiveButton("Delete"){_,_ ->
                notesViewModel.DelNote(currentNote)
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }

}