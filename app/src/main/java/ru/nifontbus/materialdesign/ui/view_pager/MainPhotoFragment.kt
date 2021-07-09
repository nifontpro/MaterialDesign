package ru.nifontbus.materialdesign.ui.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nifontbus.materialdesign.databinding.FragmentApiBinding

class MainPhotoFragment: Fragment() {
    private var _binding: FragmentApiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = PhotoAdapter(childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.currentItem = 2

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}