package ru.nifontbus.materialdesign.ui.recycler.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.broadcast.myapplication.adapter.decorations.FeedHorizontalDividerItemDecoration
import com.broadcast.myapplication.adapter.decorations.GroupVerticalItemDecoration
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

            addItemDecoration(FeedHorizontalDividerItemDecoration(10)) // addable
            addItemDecoration(GroupVerticalItemDecoration(TYPE_NOTE, 20, 0)) // addable
            addItemDecoration(GroupVerticalItemDecoration(TYPE_HEADER, 0, 10)) // addable
        }

        binding.recyclerFAB.setOnClickListener { adapter.appendItem(currentNote) }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerDiffUtilFAB.setOnClickListener { destroyFragment() }
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        adapter.viewModel = viewModel
    }

    private fun renderData(list: MutableList<Note>) {
        adapter.setItems(list)
//        adapter.submitList(list)
    }

    private fun destroyFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
