package ru.itis.homeworks.application3.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.itis.homeworks.application3.data.QuestionDatabase
import ru.itis.homeworks.application3.questionnaire.QuestionnaireFragment

class ViewPagerAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    private val questionList = QuestionDatabase.questionList

    override fun createFragment(position: Int): Fragment {
        return QuestionnaireFragment.getInstance(position)
    }

    override fun getItemCount(): Int = questionList.size
}