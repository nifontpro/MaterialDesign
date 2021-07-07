package ru.nifontbus.materialdesign.ui.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.ActivityApiBinding

class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

/*        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_system)*/

    }
}
