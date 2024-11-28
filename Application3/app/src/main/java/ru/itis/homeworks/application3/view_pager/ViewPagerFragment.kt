package ru.itis.homeworks.application3.view_pager

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import ru.itis.homeworks.application3.R
import ru.itis.homeworks.application3.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private var viewBinding: FragmentViewPagerBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentViewPagerBinding.bind(view)

        val adapter = ViewPagerAdapter(
            manager = parentFragmentManager,
            lifecycle = this.lifecycle
        )
        viewBinding?.apply {
            viewPager.adapter = adapter
        }

        viewBinding?.apply {
            //отслеживаем, когда пользователь листает фрагменты. Внутри метода onPageSelected() позиция текущего фрагмента
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    tvTitle.text = context?.getString(R.string.question_count, position + 1, adapter.itemCount)
                    buttonBack.isEnabled = position > 0

                    if (position == adapter.itemCount - 1) {
                        buttonNext.setText(R.string.btn_finish_text)
                        buttonNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.item_selected_color))
                    } else {
                        buttonNext.setText(R.string.btn_next_text)
                        buttonNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_color))
                    }
                }
            })

            buttonBack.setOnClickListener {
                if (viewPager.currentItem > 0) {
                    viewPager.currentItem -= 1
                }
            }

            buttonNext.setOnClickListener {
                if (viewPager.currentItem < (adapter.itemCount - 1)) {
                    viewPager.currentItem += 1
                } else {
                    Snackbar.make(root, R.string.snackbar_message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}