package ru.nifontbus.materialdesign

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StyleRes
import ru.nifontbus.materialdesign.data.ThemeHolder
import ru.nifontbus.materialdesign.ui.picture.PictureOfTheDayFragment

const val THEME_KEY = "MyTheme"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) loadTheme()
        else setTheme(ThemeHolder.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            Log.e("my", "savedInstanceState == null!!!")
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    private fun loadTheme() {
        val theme = getPreferences(Context.MODE_PRIVATE).getInt(THEME_KEY, R.style.GrayTheme)
        ThemeHolder.theme = theme
        setTheme(theme)
    }
}