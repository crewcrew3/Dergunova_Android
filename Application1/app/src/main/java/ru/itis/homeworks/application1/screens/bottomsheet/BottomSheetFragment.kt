package ru.itis.homeworks.application1.screens.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.itis.homeworks.application1.Properties
import ru.itis.homeworks.application1.R
import ru.itis.homeworks.application1.activities.MainActivity
import ru.itis.homeworks.application1.databinding.DialogBottomSheetBinding
import ru.itis.homeworks.application1.screens.mainpages.FirstFragment
import ru.itis.homeworks.application1.utils.NavigationAction

class BottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_bottom_sheet) {

    private var viewBinding: DialogBottomSheetBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DialogBottomSheetBinding.bind(view)

        viewBinding?.apply {

            buttonSend.isEnabled = false

            editText.doAfterTextChanged { s ->
                buttonSend.isEnabled = !s.isNullOrEmpty()
            }

            buttonSend.setOnClickListener {

                val activity = requireActivity() as? MainActivity
                //без проверки на пустую строку тк кнопка не будет работать, если она пустая
                val messageFromBottomSheet = editText.text.toString()
                activity?.navigate(
                    destination = FirstFragment.getInstance(messageFromBottomSheet),
                    destinationTag = Properties.TAG_FIRST_BS,
                    action = NavigationAction.REPLACE,
                    isAddToBackStack = false
                )
            }
        }
    }
}