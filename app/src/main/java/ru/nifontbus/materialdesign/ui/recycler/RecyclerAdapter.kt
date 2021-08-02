package ru.nifontbus.materialdesign.ui.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.databinding.RecyclerItemEarthBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemHeaderBinding
import ru.nifontbus.materialdesign.databinding.RecyclerItemMarsBinding
import ru.nifontbus.materialdesign.ui.picture.hide
import ru.nifontbus.materialdesign.ui.picture.show
import kotlin.properties.Delegates

class RecyclerAdapter(

    private val onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Data>,
    private val dragListener: OnStartDragListener

) : RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder>(),
    ItemTouchHelperAdapter,
    AutoUpdatableAdapter {

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

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
/*            if (payloads.any { it is Data }) {
                Log.e("my", "Old data: ${data[position]}")
                data[position] = payloads[payloads.lastIndex] as Data
                Log.e("my", "New data: ${data[position]}")
                holder.changeSomeText(data[position].someText)
            }*/

            val combinedChange =
                createCombinedPayload(payloads as List<Change<Data>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.someText != oldData.someText) {
                holder.changeSomeText(newData.someText)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
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

    fun setItems(newItems: List<Data>) {
//        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
//        result.dispatchUpdatesTo(this)
        autoNotify(data, newItems) { oldItem, newItem -> oldItem.id == newItem.id }
        data.clear()
        data.addAll(newItems)
    }


    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem(): Data {
        val data = Data("Mars")
        Log.e("my", "ID = ${data.id}")
        return data
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {

        abstract fun bind(dataItem: Data)
        abstract fun changeSomeText(text: String)

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
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(dataItem: Data) {
            binding.root.setOnClickListener {
                notifyItemChanged(1, Data("Jupiter"))
            }
        }

        override fun onItemSelected() {}
        override fun onItemClear() {}
        override fun changeSomeText(text: String) {}
    }

    inner class EarthViewHolder(val binding: RecyclerItemEarthBinding) :
        BaseViewHolder(binding.root) {

        override fun changeSomeText(text: String) {}

        override fun bind(dataItem: Data) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.descriptionTextView.text = dataItem.someDescription
                binding.wikiImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(
                        dataItem
                    )
                }
            }
        }
    }

    inner class MarsViewHolder(val binding: RecyclerItemMarsBinding) :
        BaseViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(dataItem: Data) {
            binding.root.setOnClickListener { onListItemClickListener.onItemClick(dataItem) }
            binding.addItemImageView.setOnClickListener { addItem() }
            binding.removeItemImageView.setOnClickListener { removeItem() }
            binding.moveItemDown.setOnClickListener { moveDown() }
            binding.moveItemUp.setOnClickListener { moveUp() }
            if (dataItem.deployed) binding.marsDescriptionTextView.show()
            else binding.marsDescriptionTextView.hide()
            binding.marsTextView.text = dataItem.someText
            binding.marsTextView.setOnClickListener { toggleText() }

            binding.dragHandleImageView.setOnTouchListener { _, event ->
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }

        override fun changeSomeText(text: String) {
            binding.marsTextView.text = text
        }

        private fun toggleText() {
            data[layoutPosition].deployed = data[layoutPosition].let {
                !it.deployed
            }
            notifyItemChanged(layoutPosition)
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Data)
    }
}