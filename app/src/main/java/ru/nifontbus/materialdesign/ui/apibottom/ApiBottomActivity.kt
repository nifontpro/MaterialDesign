package ru.nifontbus.materialdesign.ui.apibottom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.ActivityApiBottomBinding

class ApiBottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}
