package ru.nifontbus.materialdesign.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.nifontbus.materialdesign.databinding.FragmentRecyclerBinding

class RecyclerFragment: Fragment() {
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arrayListOf(
            Data("Earth"),
            Data("Earth"),
            Data("Mars", ""),
            Data("Earth"),
            Data("Earth"),
            Data("Earth"),
            Data("Mars", null)
        )
        binding.recyclerView.adapter = RecyclerAdapter(
            object : OnListItemClickListener{
                override fun onItemClick(data: Data) {
                    Toast.makeText(requireContext(), data.someText, Toast.LENGTH_SHORT).show()
                }
            },
            data
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
