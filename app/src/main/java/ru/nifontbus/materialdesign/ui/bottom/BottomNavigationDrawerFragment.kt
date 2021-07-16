package ru.nifontbus.materialdesign.ui.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.BottomNavigationLayoutBinding
import ru.nifontbus.materialdesign.ui.animations.one.AnimationsFragment
import ru.nifontbus.materialdesign.ui.animations.one.AnimationsFragmentImage
import ru.nifontbus.materialdesign.ui.animations.two.AnimationsFragmentBonus
import ru.nifontbus.materialdesign.ui.picture.replaceFragment
import ru.nifontbus.materialdesign.ui.recycler.RecyclerFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> replaceFragment(AnimationsFragment())
                R.id.navigation_two -> replaceFragment(AnimationsFragmentBonus())
                R.id.navigation_three  -> replaceFragment(RecyclerFragment())
            }
            dismiss()
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
