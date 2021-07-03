package ru.nifontbus.materialdesign.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.data.ThemeHolder
import ru.nifontbus.materialdesign.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                ThemeHolder.theme = when(it.id) {
                    R.id.th_sea -> R.style.SeaTheme
                    R.id.th_indigo -> R.style.IndigoTheme
                    R.id.th_gray -> R.style.GrayTheme
                    else -> 0
                }
            }
        }

        binding.applyBtn.setOnClickListener {
            requireActivity().recreate()
        }

        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(
                context,
                "Close is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}