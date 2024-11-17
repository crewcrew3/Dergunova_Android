package ru.itis.homeworks.application2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.itis.homeworks.application2.databinding.ActivityMainBinding
import ru.itis.homeworks.application2.screens.SongCatalogFragment

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    private val mainContainerId: Int = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(mainContainerId, SongCatalogFragment(), Properties.CATALOG_TAG).commit()
        }
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}