package ru.itis.homeworks.application3.questionnaire

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application3.Properties
import ru.itis.homeworks.application3.R
import ru.itis.homeworks.application3.data.QuestionDatabase
import ru.itis.homeworks.application3.databinding.FragmentQuestionnaireBinding
import ru.itis.homeworks.application3.recycler_view.AnswerAdapter

class QuestionnaireFragment : Fragment(R.layout.fragment_questionnaire) {

    private var viewBinding: FragmentQuestionnaireBinding? = null
    private var adapter: AnswerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentQuestionnaireBinding.bind(view)

        val position = arguments?.getInt(Properties.POSITION_KEY)
        val question = QuestionDatabase.questionList.find{it.id == position} ?: return

        viewBinding?.apply {
            tvQuestion.text = question.text

            adapter = AnswerAdapter(
                items = question.answers,
            )
            rvAnswers.adapter = adapter
            rvAnswers.layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    companion object {
        fun getInstance(position: Int) = QuestionnaireFragment().apply {
            arguments = bundleOf(Properties.POSITION_KEY to position)
        }
    }
}