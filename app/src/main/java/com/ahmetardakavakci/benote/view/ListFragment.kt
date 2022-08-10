package com.ahmetardakavakci.benote.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

    private lateinit var db: DatabaseNote
    private lateinit var allNotes : List<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseNote.getDatabase(requireContext())
        allNotes = db.daoNote().getAll()
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

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.notesRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = NotesAdapter(allNotes)
        }

        if (binding.notesRecycler.adapter != null) {
            binding.notesRecycler.adapter!!.notifyDataSetChanged()
            println("adapter is not empty")
        } else {
            println("adapter is null")
        }



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