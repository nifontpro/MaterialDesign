package ru.nifontbus.materialdesign.ui.recycler.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.nifontbus.materialdesign.ui.recycler.notes.decorations.FeedHorizontalDividerItemDecoration
import ru.nifontbus.materialdesign.ui.recycler.notes.decorations.GroupVerticalItemDecoration
import ru.nifontbus.materialdesign.databinding.FragmentRecyclerBinding
import ru.nifontbus.materialdesign.ui.picture.OnSetDateInMainFragment
import ru.nifontbus.materialdesign.ui.recycler.ItemTouchHelperCallback
import ru.nifontbus.materialdesign.ui.recycler.TYPE_HEADER

// https://android--code.blogspot.com/2019/02/android-kotlin-room-recyclerview.html
class NotesFragment(
    private val currentNote: Note,
    private val onSetDateInMainFragment: OnSetDateInMainFragment
) : Fragment() {

    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: NotesAdapter
    private val viewModel: NotesViewModel by lazy { ViewModelProvider(this).get(NotesViewModel::class.java) }

    // https://android--code.blogspot.com/2019/02/android-kotlin-room-recyclerview.html

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesAdapter(onSetDateInMainFragment)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        with(binding.recyclerView) {
            adapter = this@NotesFragment.adapter

            addItemDecoration(FeedHorizontalDividerItemDecoration(20)) // addable
            addItemDecoration(GroupVerticalItemDecoration(TYPE_NOTE, 20, 20)) // addable
            addItemDecoration(GroupVerticalItemDecoration(TYPE_HEADER, 0, 30)) // addable
        }

        binding.recyclerFAB.setOnClickListener { adapter.appendItem(currentNote) }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerDiffUtilFAB.setOnClickListener { destroyFragment() }
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        adapter.viewModel = viewModel
    }

    private fun renderData(list: MutableList<Note>) {
//        adapter.setItems(list)
        Log.d("my", "read all: $list")
        adapter.submitList(list.toList())

    }

    private fun destroyFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
