package ru.nifontbus.materialdesign.ui.recycler.notes

import androidx.recyclerview.widget.DiffUtil

// Diff для использования ListAdapter
class DiffCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}