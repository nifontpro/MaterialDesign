package ru.nifontbus.materialdesign.ui.recycler.notes

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.app.App.Companion.getHistoryDao
import ru.nifontbus.materialdesign.databinding.ItemNoteBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemHeaderBinding
import ru.nifontbus.materialdesign.ui.picture.OnSetDateInMainFragment
import ru.nifontbus.materialdesign.ui.picture.hide
import ru.nifontbus.materialdesign.ui.picture.show
import ru.nifontbus.materialdesign.ui.recycler.ItemTouchHelperAdapter
import ru.nifontbus.materialdesign.ui.recycler.ItemTouchHelperViewHolder
import ru.nifontbus.materialdesign.ui.recycler.TYPE_HEADER
import ru.nifontbus.materialdesign.ui.recycler.notes.room.Converters
import ru.nifontbus.materialdesign.ui.recycler.notes.room.LocalRepository
import ru.nifontbus.materialdesign.ui.recycler.notes.room.LocalRepositoryImpl
import java.time.format.DateTimeFormatter

class NotesAdapter(

    private val onSetDateInMainFragment: OnSetDateInMainFragment

) : RecyclerView.Adapter<NotesAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

    private var notes: MutableList<Note>
    private val localRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())

    init {
        notes = localRepository.getAllNotes().toMutableList()
        notes.add(0, Note("Header", type = TYPE_HEADER))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = RecyclerItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemNoteBinding.inflate(inflater, parent, false)
                NoteViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun getItemViewType(position: Int): Int {
        return notes[position].type
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun onItemDismiss(position: Int) {
        if (position > 0) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        } else notifyItemChanged(position)
    }

    fun appendItem(note: Note) {
        note.deployed = false
        notes.add(note)
        notifyItemInserted(itemCount - 1)
        localRepository.saveNote(note)
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {

        abstract fun bind(note: Note)
        abstract fun changeSomeText(text: String)

        fun removeItem() {
            notes.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(note: Note) {
            binding.header.text = "Избранное"
        }
        override fun onItemSelected() {}
        override fun onItemClear() {}
        override fun changeSomeText(text: String) {}
    }

    inner class NoteViewHolder(val binding: ItemNoteBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvDescription.text = note.description
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            binding.tvDate.text = note.date.format(formatter)

            binding.removeImg.setOnClickListener { removeItem() }
            binding.root.setOnClickListener { toggleText() }
            if (note.deployed) binding.tvDescription.show()
            else binding.tvDescription.hide()
            binding.imageView.setOnClickListener {
                onSetDateInMainFragment.setDate(note.date)}
        }

        override fun changeSomeText(text: String) {
            binding.tvTitle.text = text
        }

        private fun toggleText() {
            notes[layoutPosition].deployed = notes[layoutPosition].let {
                !it.deployed
            }
            notifyItemChanged(layoutPosition)
        }
    }
}