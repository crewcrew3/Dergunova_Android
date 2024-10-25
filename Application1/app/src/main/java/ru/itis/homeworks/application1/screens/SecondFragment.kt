package ru.itis.homeworks.application1.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.itis.homeworks.application1.activities.MainActivity
import ru.itis.homeworks.application1.utils.NavigationAction
import ru.itis.homeworks.application1.Properties
import ru.itis.homeworks.application1.R
import ru.itis.homeworks.application1.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {

    private var viewBinding: FragmentSecondBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSecondBinding.bind(view)

        viewBinding?.apply {
            val message = arguments?.getString(Properties.ARG_TEXT) ?: Properties.DEFAULT_TEXT
            text.text = message
            val activity = requireActivity() as? MainActivity

            buttonToThirdScreen.setOnClickListener {
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

    companion object {
        fun getInstance(
            text: String
        ): SecondFragment {
            val bundle = Bundle()
            bundle.putString(Properties.ARG_TEXT, text)
            return SecondFragment().apply {
                arguments = bundle
            }
        }
    }
}