package ru.itis.homeworks.application1.activities

import android.os.Bundle
import ru.itis.homeworks.application1.utils.FragmentLifecycleListener
import ru.itis.homeworks.application1.utils.NavigationAction
import ru.itis.homeworks.application1.Properties
import ru.itis.homeworks.application1.R
import ru.itis.homeworks.application1.databinding.ActivityMainBinding
import ru.itis.homeworks.application1.screens.mainpages.FirstFragment

class MainActivity : BaseActivity() {

    private var viewBinding: ActivityMainBinding? = null

    override val containerId: Int = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            FragmentLifecycleListener(),
            false
        )

        if (savedInstanceState == null) {
            navigate(
                destination = FirstFragment(),
                destinationTag = Properties.TAG_FIRST,
                action = NavigationAction.ADD
            )
        }
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}