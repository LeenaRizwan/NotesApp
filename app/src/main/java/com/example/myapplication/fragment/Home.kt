package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.NoteViewModel


class Home : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    //Bind home layout to home fragment
    private var homeBinding: FragmentHomeBinding?=null
    private val binding get() = homeBinding!!
    //var for note view model and adapter
    //View Model is business logic layer,
    //DAO is database layer, NoteDB is database itself, setting up table and cols
    private lateinit var notesviewmodel: NoteViewModel
    private lateinit var noteAdapter:NoteAdapter

    //For oncreateview,
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner,Lifecycle.State.RESUMED)

        notesviewmodel = (activity as MainActivity).noteViewModel
        setupHomeRecyclerView()

        binding.addNoteFab.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }
    //Now 8 functions???

    //First is to toggle between empty notes and recycler view
    private fun updateUI(note: List<Note>?){
        if(note!=null){
            if(note.isNotEmpty()){
                binding.emptyNotesImage.visibility=View.GONE
                binding.homeRecyclerView.visibility=View.VISIBLE
            }
            else{
                binding.emptyNotesImage.visibility=View.VISIBLE
                binding.homeRecyclerView.visibility=View.GONE
            }
        }
    }

    //Recycler view Function
    private fun setupHomeRecyclerView(){
        noteAdapter= NoteAdapter()
        binding.homeRecyclerView.apply{

            layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter =noteAdapter
        }
        activity?.let{
            notesviewmodel.getAllNotes().observe(viewLifecycleOwner){ note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private fun searchNote(query:String?){
        val searchquery = "%$query"

        notesviewmodel.searchNote(searchquery).observe(this){list ->
            noteAdapter.differ.submitList(list)
        }
    }

    //Search View functions!
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding =null
    }

    //clear existing menu and reset the whole thing
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
        val menuSearch =menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    //false, because search isnt enabled by this button
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}