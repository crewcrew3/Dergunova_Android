package ru.itis.application5.compose

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.itis.application5.utils.CoroutinesParameters
import ru.itis.application5.R
import ru.itis.application5.compose.ui.ButtonContent
import ru.itis.application5.compose.ui.DropDownMenu
import ru.itis.application5.compose.ui.MainContent
import ru.itis.application5.databinding.FragmentComposeBinding
import java.util.concurrent.CopyOnWriteArrayList

class ComposeFragment : BaseFragment(R.layout.fragment_compose), LifecycleObserver {

    private var viewBinding: FragmentComposeBinding? = null
    private var parentJob: Job? = null
    private var jobList = CopyOnWriteArrayList<Job>() //потокобезопасный список

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentComposeBinding.bind(view)

        //подписываемся на события жизненного цикла, чтобы завершать корутины при сворачивании приложения, если нам это необходимо
        lifecycle.addObserver(this)

        val pullThreadsList = listOf(
            Dispatchers.IO,
            Dispatchers.Default,
            Dispatchers.Main,
            Dispatchers.Unconfined
        )

        viewBinding?.composeContainerId?.setContent {
            Surface {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 240.dp)
                ) {
                    MainContent()
                    DropDownMenu(pullThreadsList)
                    ButtonContent(
                        onClickRun = ::onClickRun,
                        onClickCancel = ::onClickCancel
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
        viewBinding = null
    }

    private fun onClickRun() {
        val numberOfCoroutines = CoroutinesParameters.numberOfCoroutines
        val isParallel = CoroutinesParameters.isParallel
        val isContinue = CoroutinesParameters.isContinue
        val pullThread = CoroutinesParameters.pullThread

        if (isParallel == null || isContinue == null || pullThread == null || numberOfCoroutines == null) {
            showAlertToChooseParameters()
        } else {
            jobList.clear()
            parentJob = CoroutineScope(pullThread)
                .launch {
                    if (isParallel) {
                        repeat(numberOfCoroutines) { count ->
                            jobList.add(
                                launch {
                                    runCatching {
                                        delay(2000L)
                                    }.onSuccess {
                                        println("TEST TAG - параллельно: $count")
                                    }.onFailure { exception ->
                                        Toast.makeText(
                                            requireContext(),
                                            exception.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    } else {
                        repeat(numberOfCoroutines) { count ->
                            jobList.add(
                                launch(start = CoroutineStart.LAZY) {
                                    runCatching {
                                        delay(2000L)
                                    }.onSuccess {
                                        println("TEST TAG - последовательно: $count")
                                    }.onFailure {exception ->
                                        Toast.makeText(
                                            requireContext(),
                                            exception.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                        println("TEST TAG - сейчас дождемся выполнения всех последовательных корутин")
                        jobList.joinAll()
                    }
                }
        }
    }

    private fun onClickCancel() {
        if (jobList.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.toast_text_coroutines_havent_started_yet), Toast.LENGTH_SHORT).show()
        } else {
            var numberOfCanceledJobs = 0
            jobList.forEachIndexed { index, job ->
                if (job.isActive) {
                    if (CoroutinesParameters.isParallel == false) {
                        //посчитаем сколько корутин отменилось, включая ту, на который мы вызвали  parentJob?.cancel()
                        numberOfCanceledJobs = jobList.size - index
                        parentJob?.cancel()
                        println("TEST TAG - Позиция первой отмененной: $index")
                    } else {
                        numberOfCanceledJobs++
                        job.cancel() //если сразу отменить parentJob то счетчик посчитает только одну job
                    }
                }
            }
            jobList.clear()
            println("TEST TAG - Кол-во отмененных корутин: $numberOfCanceledJobs")
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_text_number_of_canceled_jobs, numberOfCanceledJobs),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showAlertToChooseParameters() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_dialog_coroutines_param_title))
            .setMessage(getString(R.string.alert_dialog_coroutines_param_message))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.alert_dialog_coroutines_param_positive_btn_text), null)
            .create()
            .show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        super.onStop()
        if (CoroutinesParameters.isContinue == false) {
            parentJob?.cancel()
            println("TEST TAG - Завершаем корутины")
        } else {
            println("TEST TAG - Не завершаем корутины")
        }
    }
}