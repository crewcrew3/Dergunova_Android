package ru.itis.homeworks.application1.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.itis.homeworks.application1.utils.NavigationAction

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val containerId: Int

    fun navigate(
        destination: Fragment,
        destinationTag: String,
        action: NavigationAction,
        isAddToBackStack: Boolean = true,
        backStackTag: String? = null
    ) {
        val transaction = supportFragmentManager.beginTransaction()

        when (action) {
            NavigationAction.ADD -> transaction.add(containerId, destination, destinationTag)
            NavigationAction.REPLACE -> transaction.replace(containerId, destination, destinationTag)
            NavigationAction.REMOVE -> transaction.remove(destination)
        }

        if (isAddToBackStack) {
            transaction.addToBackStack(backStackTag)
        }

        transaction.commit()
    }

}