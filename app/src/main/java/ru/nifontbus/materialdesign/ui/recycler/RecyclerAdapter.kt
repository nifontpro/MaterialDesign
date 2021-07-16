package ru.nifontbus.materialdesign.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.databinding.FragmentRecyclerItemEarthBinding
import ru.nifontbus.materialdesign.databinding.FragmentRecyclerItemMarsBinding

class RecyclerAdapter(
// https://www.geeksforgeeks.org/how-to-use-view-binding-in-recyclerview-adapter-class-in-android/
    private val onListItemClickListener: OnListItemClickListener,
    private var data: List<Data>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_EARTH) {
            val binding = FragmentRecyclerItemEarthBinding.inflate(inflater, parent, false)
            EarthViewHolder(binding)
        } else {
            val binding = FragmentRecyclerItemMarsBinding.inflate(inflater, parent, false)
            MarsViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_EARTH) {
            holder as EarthViewHolder
            holder.bind(data[position])
        } else {
            holder as MarsViewHolder
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].someDescription.isNullOrBlank()) TYPE_MARS else TYPE_EARTH
    }

    inner class EarthViewHolder(val binding: FragmentRecyclerItemEarthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Data) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.descriptionTextView.text = data.someDescription
                binding.wikiImageView.setOnClickListener { onListItemClickListener.onItemClick(data) }
            }
        }
    }

    inner class MarsViewHolder(val binding: FragmentRecyclerItemMarsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            binding.marsImageView.setOnClickListener { onListItemClickListener.onItemClick(data) }
        }
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }

}