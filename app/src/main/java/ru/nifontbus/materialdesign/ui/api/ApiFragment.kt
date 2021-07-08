package ru.nifontbus.materialdesign.ui.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nifontbus.materialdesign.databinding.FragmentApiBinding

class ApiFragment: Fragment() {
    private var _binding: FragmentApiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        /*        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_system)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}