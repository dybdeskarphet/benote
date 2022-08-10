package com.ahmetardakavakci.benote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmetardakavakci.benote.NotesAdapter.*
import com.ahmetardakavakci.benote.databinding.RecyclerLineBinding
import com.ahmetardakavakci.benote.db.Note
import com.ahmetardakavakci.benote.view.ListFragmentDirections

class NotesAdapter(val allNotes: List<Note>) : RecyclerView.Adapter<NotesHolder>() {

    class NotesHolder(val binding: RecyclerLineBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val binding = RecyclerLineBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotesHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.binding.titleNote.text = allNotes[position].titleString
        holder.binding.contentNote.text = allNotes[position].contentString

        if (allNotes[position].colorInt == 0) {
            holder.binding.titleNote.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_light))
            holder.binding.contentNote.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_light))
        } else {
            holder.binding.titleNote.setBackgroundColor(allNotes[position].colorInt!!)
            holder.binding.contentNote.setBackgroundColor(allNotes[position].colorInt!!)
        }

        holder.binding.cardView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToContentFragment(allNotes[position].id!!,false)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount() = allNotes.size

}