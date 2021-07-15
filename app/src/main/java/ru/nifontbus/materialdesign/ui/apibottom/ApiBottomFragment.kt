package ru.nifontbus.materialdesign.ui.apibottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import geekbarains.material.ui.api.MarsFragment
import geekbarains.material.ui.api.WeatherFragment
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.FragmentApiBinding
import ru.nifontbus.materialdesign.databinding.FragmentApiBottomBinding
import ru.nifontbus.materialdesign.ui.api.EarthFragment

class ApiBottomFragment : Fragment() {
    private var _binding: FragmentApiBottomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApiBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(EarthFragment())
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    replaceFragment(EarthFragment())
                    true

                }
                R.id.bottom_view_mars -> {
                    replaceFragment(MarsFragment())
                    true
                }
                R.id.bottom_view_weather -> {
                    replaceFragment(WeatherFragment())
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigationView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    //Item tapped
                }
                R.id.bottom_view_mars -> {
                    //Item tapped
                }
                R.id.bottom_view_weather -> {
                    //Item tapped
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.activity_api_bottom_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}