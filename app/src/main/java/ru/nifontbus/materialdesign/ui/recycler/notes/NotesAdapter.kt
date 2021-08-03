package ru.nifontbus.materialdesign.ui.recycler.notes

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.nifontbus.materialdesign.databinding.ItemNoteBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemHeaderBinding
import ru.nifontbus.materialdesign.ui.picture.OnSetDateInMainFragment
import ru.nifontbus.materialdesign.ui.picture.hide
import ru.nifontbus.materialdesign.ui.picture.show
import ru.nifontbus.materialdesign.ui.recycler.AutoUpdatableAdapter
import ru.nifontbus.materialdesign.ui.recycler.ItemTouchHelperAdapter
import ru.nifontbus.materialdesign.ui.recycler.ItemTouchHelperViewHolder
import ru.nifontbus.materialdesign.ui.recycler.TYPE_HEADER
import java.time.format.DateTimeFormatter

class NotesAdapter(

    private val onSetDateInMainFragment: OnSetDateInMainFragment

) : ListAdapter<Note, NotesAdapter.BaseViewHolder<ViewBinding>>(DiffCallback()),
    ItemTouchHelperAdapter,
    AutoUpdatableAdapter {

    lateinit var viewModel: NotesViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {

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

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun onItemDismiss(position: Int) {
        if (position == 0) {
            notifyItemChanged(0)
        } else {
            viewModel.deleteByPosition(position)
        }
    }

    fun appendItem(note: Note) {
        note.deployed = false
        viewModel.insert(note)
    }

    abstract inner class BaseViewHolder<out V : ViewBinding>(
        val binding: V
    ) : RecyclerView.ViewHolder(binding.root),
        ItemTouchHelperViewHolder {

        abstract fun bind(note: Note)
        abstract fun changeSomeText(text: String)

        fun removeItem() {
            viewModel.deleteByPosition(layoutPosition)
        }

        override fun onItemSelected() {
            binding.root.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            binding.root.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(binding: RecyclerItemHeaderBinding) :
        BaseViewHolder<RecyclerItemHeaderBinding>(binding) {

        override fun bind(note: Note) {
            binding.header.text = "Избранное"
        }

        override fun onItemSelected() {}
        override fun onItemClear() {}
        override fun changeSomeText(text: String) {}
    }

    inner class NoteViewHolder(binding: ItemNoteBinding) :
        BaseViewHolder<ItemNoteBinding>(binding) {

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
                onSetDateInMainFragment.setDate(note.date)
            }
        }

        override fun changeSomeText(text: String) {
            binding.tvTitle.text = text
        }

        private fun toggleText() {
            currentList[layoutPosition].deployed = currentList[layoutPosition].let {
                !it.deployed
            }
            notifyItemChanged(layoutPosition)
        }
    }
}