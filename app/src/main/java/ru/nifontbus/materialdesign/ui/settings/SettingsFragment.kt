package ru.nifontbus.materialdesign.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.THEME_KEY
import ru.nifontbus.materialdesign.data.ThemeHolder
import ru.nifontbus.materialdesign.databinding.FragmentSettingsBinding

const val NOT_SELECTED = -1

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var theme = ThemeHolder.theme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setThemeChip()
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                theme = when (it.id) {
                    R.id.th_sea -> R.style.SeaTheme
                    R.id.th_indigo -> R.style.IndigoTheme
                    R.id.th_gray -> R.style.GrayTheme
                    else -> NOT_SELECTED
                }
            }
        }

        binding.applyBtn.setOnClickListener {
            if (theme == NOT_SELECTED || theme == ThemeHolder.theme) return@setOnClickListener
            ThemeHolder.theme = theme
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putInt(THEME_KEY, theme)
                    apply()
                }
                it.recreate()
            }
        }

        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(
                context,
                "Close is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setThemeChip() {
        when (ThemeHolder.theme) {
            R.style.SeaTheme -> binding.thSea.isChecked = true
            R.style.IndigoTheme -> binding.thIndigo.isChecked = true
            R.style.GrayTheme -> binding.thGray.isChecked = true
        }
    }
}