package ru.itis.homeworks.application1.screens.mainpages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.itis.homeworks.application1.Properties
import ru.itis.homeworks.application1.R
import ru.itis.homeworks.application1.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(R.layout.fragment_third) {

    private var viewBinding: FragmentThirdBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentThirdBinding.bind(view)

        viewBinding?.apply {
            val message = arguments?.getString(Properties.ARG_TEXT)
            text.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        fun getInstance(
            text: String?
        ): ThirdFragment {
            val bundle = Bundle()
            bundle.putString(Properties.ARG_TEXT, text)
            return ThirdFragment().apply {
                arguments = bundle
            }
        }
    }
}