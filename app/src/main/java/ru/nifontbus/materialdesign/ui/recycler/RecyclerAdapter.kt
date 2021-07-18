package ru.nifontbus.materialdesign.ui.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.databinding.RecyclerItemEarthBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemHeaderBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemMarsBinding
import ru.nifontbus.materialdesign.ui.picture.hide
import ru.nifontbus.materialdesign.ui.picture.show

class RecyclerAdapter(

    private val onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Data>,

    ) : RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // https://www.geeksforgeeks.org/how-to-use-view-binding-in-recyclerview-adapter-class-in-android/
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> {
                val binding = RecyclerItemEarthBinding.inflate(inflater, parent, false)
                EarthViewHolder(binding)
            }
            TYPE_MARS -> {
                val binding = RecyclerItemMarsBinding.inflate(inflater, parent, false)
                MarsViewHolder(binding)
            }
            else -> {
                val binding = RecyclerItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_MARS
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition == 1 && toPosition == 0) return
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        if (position > 0) {
            data.removeAt(position)
            notifyItemRemoved(position)
        } else notifyItemChanged(position)
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {

        abstract fun bind(data: Data)

        fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(data: Data) {
            binding.root.setOnClickListener { onListItemClickListener.onItemClick(data) }
        }
    }

    inner class EarthViewHolder(val binding: RecyclerItemEarthBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(data: Data) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.descriptionTextView.text = data.someDescription
                binding.wikiImageView.setOnClickListener { onListItemClickListener.onItemClick(data) }
            }
        }
    }

    inner class MarsViewHolder(val binding: RecyclerItemMarsBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(data: Data) {
            binding.root.setOnClickListener { onListItemClickListener.onItemClick(data) }
            binding.addItemImageView.setOnClickListener { addItem() }
            binding.removeItemImageView.setOnClickListener { removeItem() }
            binding.moveItemDown.setOnClickListener { moveDown() }
            binding.moveItemUp.setOnClickListener { moveUp() }
            if (data.deployed) binding.marsDescriptionTextView.show()
            else binding.marsDescriptionTextView.hide()
            binding.marsTextView.setOnClickListener { toggleText() }
        }

        private fun toggleText() {
            data[layoutPosition].deployed = data[layoutPosition].let {
                !it.deployed
            }
            notifyItemChanged(layoutPosition)
        }
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem(): Data = Data("Mars", "")

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }
}