package ru.itis.application7.customview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.itis.application7.customview.databinding.FragmentPieChartViewBinding
//все что для вьюшки находится в res
class PieChartFragment : Fragment(R.layout.fragment_pie_chart_view) {

    private var viewBinding: FragmentPieChartViewBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentPieChartViewBinding.bind(view)

        viewBinding?.apply {
            btnDraw.setOnClickListener {

                val inputText = etInput.text.toString().trim()
                if (inputText.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.error_empty_input), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val parts = inputText.split("\\s+".toRegex())

                val numbers = mutableListOf<Float>()
                for (part in parts) {
                    val num = part.toFloatOrNull()
                    if (num == null) {
                        Toast.makeText(requireContext(), getString(R.string.error_invalid_input), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else {
                        numbers.add(num)
                    }
                }

                val sum = numbers.sum()
                if (sum != 100f) { //в самой вьюшке я кидаю эксепшон. Тут как бы валидация в самом представлении, тк как будто если пользователь что-то не то ввел, приложение не должно сразу падать
                    Toast.makeText(requireContext(), getString(R.string.error_sum_not_100), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val map = numbers.mapIndexed { index, value -> (index + 1) to value }.toMap()

                pieChart.pieData = map
                pieChart.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}