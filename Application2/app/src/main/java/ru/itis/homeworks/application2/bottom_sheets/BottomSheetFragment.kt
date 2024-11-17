package ru.itis.homeworks.application2.bottom_sheets

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.itis.homeworks.application2.MainActivity
import ru.itis.homeworks.application2.Properties
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.DialogBottomSheetBinding
import ru.itis.homeworks.application2.screens.SongCatalogFragment

class BottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_bottom_sheet) {

    private var viewBinding : DialogBottomSheetBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DialogBottomSheetBinding.bind(view)

        val catalogFragment = requireActivity().supportFragmentManager.findFragmentByTag(Properties.CATALOG_TAG) as? SongCatalogFragment

        viewBinding?.apply {

            btnAdd.setOnClickListener {
                val count = editText.text.toString().toIntOrNull() ?: 0
                catalogFragment?.add(count)
                dismiss()
            }

            btnDelete.setOnClickListener {
                val count = editText.text.toString().toIntOrNull() ?: 0
                catalogFragment?.delete(count)
                dismiss()
            }

            btnAddOne.setOnClickListener {
                catalogFragment?.add(1)
                dismiss()
            }

            btnDeleteOne.setOnClickListener {
                catalogFragment?.delete(1)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}