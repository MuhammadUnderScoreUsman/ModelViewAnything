package com.mohammadosman.modelviewanything.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohammadosman.modelviewanything.databinding.ActivityMainBinding
import com.mohammadosman.modelviewanything.presentation.mvp.MvpActivity
import com.mohammadosman.modelviewanything.presentation.mvpToMVI.MvpToMviActivity
import com.mohammadosman.modelviewanything.presentation.mvvmToMVI.MvvmToMviActivity

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {

            btnMvp.setOnClickListener {
                Intent(this@MainActivity,
                    MvpActivity::class.java).apply {
                    startActivity(this)
                }
            }

            btnMvpToMvi.setOnClickListener {
                Intent(this@MainActivity, MvpToMviActivity::class.java).apply {
                    startActivity(this)
                }
            }
            btnMvvmToMvi.setOnClickListener {
                Intent(this@MainActivity, MvvmToMviActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}