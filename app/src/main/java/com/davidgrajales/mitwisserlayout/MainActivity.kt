package com.davidgrajales.mitwisserlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { ViewPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(tab_layout, pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Mensajes"
                    }
                    1 -> {
                        tab.text = "Opciones"
                    }

                }
            })
        tabLayoutMediator.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}