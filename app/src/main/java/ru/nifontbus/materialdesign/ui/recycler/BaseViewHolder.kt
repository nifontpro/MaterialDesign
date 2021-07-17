package geekbarains.material.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.ui.recycler.Data

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: Data)
}
