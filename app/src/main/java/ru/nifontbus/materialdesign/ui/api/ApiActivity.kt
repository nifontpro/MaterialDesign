package ru.nifontbus.materialdesign.ui.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nifontbus.materialdesign.databinding.ActivityApiBinding

class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
    }
}
