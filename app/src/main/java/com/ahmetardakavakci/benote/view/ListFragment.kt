package com.ahmetardakavakci.benote.view

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetardakavakci.benote.NotesAdapter
import com.ahmetardakavakci.benote.R
import com.ahmetardakavakci.benote.databinding.FragmentListBinding
import com.ahmetardakavakci.benote.db.DatabaseNote
import com.ahmetardakavakci.benote.db.Note

class ListFragment : Fragment() {

    // binding related
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    lateinit private var notesAdapter: NotesAdapter

    private lateinit var db: DatabaseNote
    private lateinit var allNotes: List<Note>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val ft = fragmentManager?.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) ft?.setReorderingAllowed(false)
        println("Refresh started")
        ft?.detach(this)?.attach(this)?.commit()
        println("Refresh finished")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseNote.getDatabase(requireContext())
        allNotes = db.daoNote().getAll()
        notesAdapter = NotesAdapter(allNotes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addFab.setOnClickListener { addFab(view) }

        println("onViewCreated")
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.notesRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = notesAdapter
        }

        notesAdapter.notifyDataSetChanged()

    }

    private fun addFab(view: View) {
        val action = ListFragmentDirections
            .actionListFragmentToContentFragment(0,true)
        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}