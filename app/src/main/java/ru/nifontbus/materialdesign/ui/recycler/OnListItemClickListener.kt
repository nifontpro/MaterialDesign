package ru.nifontbus.materialdesign.ui.recycler

interface OnListItemClickListener {
    fun onItemClick(data: Data)
}

interface ItemTouchHelperAdapter {
    // onItemMove будет вызываться, когда элемент списка будет перетянут на
    // достаточное расстояние, чтобы запустить анимацию перемещения.
    fun onItemMove(fromPosition: Int, toPosition: Int)

    // onItemDismiss будет вызываться во время свайпа по элементу.
    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {

    // onItemSelected будет вызываться в процессе смахивания или перетаскивания элемента.
    fun onItemSelected()

    // onItemClear — когда этот процесс закончится.
    fun onItemClear()
}