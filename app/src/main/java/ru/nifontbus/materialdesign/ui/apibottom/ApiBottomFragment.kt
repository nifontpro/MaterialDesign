package ru.nifontbus.materialdesign.ui.apibottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.FragmentApiBinding
import ru.nifontbus.materialdesign.databinding.FragmentApiBottomBinding

class ApiBottomFragment: Fragment() {
    private var _binding: FragmentApiBottomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =  FragmentApiBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    //Item tapped
                    true
                }
                R.id.bottom_view_mars -> {
                    //Item tapped
                    true
                }
                R.id.bottom_view_weather -> {
                    //Item tapped
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}