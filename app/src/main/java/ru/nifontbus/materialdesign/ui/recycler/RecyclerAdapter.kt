package ru.nifontbus.materialdesign.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import geekbarains.material.ui.recycler.BaseViewHolder
import ru.nifontbus.materialdesign.databinding.RecyclerItemEarthBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemHeaderBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemMarsBinding

class RecyclerAdapter(

    private val onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Data>,

    ) : RecyclerView.Adapter<BaseViewHolder>() {

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
            else -> TYPE_EARTH
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding)
        : BaseViewHolder(binding.root) {

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

        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
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