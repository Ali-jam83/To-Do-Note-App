package com.example.notepads.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.notepads.R
import com.example.notepads.databinding.ActivitySplashBinding
import com.example.notepads.ui.activity.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {


            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val attrib = window.attributes
            attrib.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        }
        else {

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

        }

        binding.imageView.startAnimation(animation(R.anim.image_splash))
        binding.textName.animation=animation(R.anim.name_splash)
        binding.textVersion.startAnimation(animation(R.anim.version_splash))

        Handler(Looper.getMainLooper()).postDelayed({


            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)


            finish()

        }, 5000)





    }


   private fun animation (id : Int): Animation {
        val anim = AnimationUtils.loadAnimation(this, id)
       return anim
    }
}