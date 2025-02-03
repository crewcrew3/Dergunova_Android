package ru.itis.application5.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import ru.itis.application5.R
import ru.itis.application5.compose.ui.ComposeScreen
import ru.itis.application5.databinding.FragmentComposeBinding

class ComposeFragment : BaseFragment(R.layout.fragment_compose) {

    private var viewBinding: FragmentComposeBinding? = null

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //создаем компоуз вью
        composeView = ComposeView(requireContext())
        //инфлейтим ее
        val view = inflater.inflate(R.layout.fragment_compose, container ,false)
        (view as? ViewGroup)?.addView(composeView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentComposeBinding.bind(view)

        viewBinding?.composeContainerId?.setContent {
            ComposeScreen()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
        viewBinding = null
    }
}