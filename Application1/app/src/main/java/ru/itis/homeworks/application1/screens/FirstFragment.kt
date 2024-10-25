package ru.itis.homeworks.application1.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.itis.homeworks.application1.activities.MainActivity
import ru.itis.homeworks.application1.utils.NavigationAction
import ru.itis.homeworks.application1.Properties
import ru.itis.homeworks.application1.R
import ru.itis.homeworks.application1.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    private var viewBinding: FragmentFirstBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentFirstBinding.bind(view)

        viewBinding?.apply {

            val activity = requireActivity() as? MainActivity

            buttonToSecondScreen.setOnClickListener {
                val inputText = editText.text.toString()
                val message = if (inputText.isEmpty()) Properties.DEFAULT_TEXT else inputText
                activity?.navigate(
                    destination = SecondFragment.getInstance(message),
                    destinationTag = Properties.TAG_SECOND,
                    action = NavigationAction.REPLACE
                    )
            }

            buttonToThirdScreen.setOnClickListener {
                val inputText = editText.text.toString()
                val message = if (inputText.isEmpty()) Properties.DEFAULT_TEXT else inputText
                activity?.navigate(
                    destination = SecondFragment.getInstance(message),
                    destinationTag = Properties.TAG_SECOND,
                    action = NavigationAction.REPLACE
                )

                activity?.navigate(
                    destination = ThirdFragment.getInstance(message),
                    destinationTag = Properties.TAG_THIRD,
                    action = NavigationAction.REPLACE
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}