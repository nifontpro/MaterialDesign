package ru.nifontbus.materialdesign

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import ru.nifontbus.materialdesign.databinding.ActivitySplashBinding

var handler = Handler(Looper.getMainLooper())

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        binding.imageView.animate().rotationBy(750f)
            .setInterpolator(LinearInterpolator()).duration = 10000


        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 3000)
        */

        binding.imageView.animate().rotationBy(200f)
            .setInterpolator(LinearInterpolator()).setDuration(500)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })

        // Вращение вокруг оси оХ
/*        binding.imageView.animate().scaleY(-1f)
            .setInterpolator(AccelerateDecelerateInterpolator()).duration = 1000

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1200)*/

    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}