package ru.nifontbus.materialdesign.ui.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PhotoAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        PhotoFragment(LocalDate.now().minusDays(3)),
        PhotoFragment(LocalDate.now().minusDays(2)),
        PhotoFragment(LocalDate.now().minusDays(1))
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0, 1, 2 -> fragments[position]
            else -> fragments[0]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateString: String =  LocalDate.now().minusDays(3).format(formatter)
        return when (position) {
            2 -> "Вчера"
            1 -> "Позавчера"
            0 -> dateString
            else -> "Вчера"
        }
    }
}
