package ru.itis.homeworks.application2.bottom_sheets

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.DialogBottomSheetBinding

class BottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_bottom_sheet) {

    private var viewBinding : DialogBottomSheetBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DialogBottomSheetBinding.bind(view)
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}