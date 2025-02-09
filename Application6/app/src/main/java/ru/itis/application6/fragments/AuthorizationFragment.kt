package ru.itis.application6.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.itis.application6.utils.Properties
import ru.itis.application6.R
import ru.itis.application6.activity.MainActivity
import ru.itis.application6.databinding.FragmentAuthorizationBinding
import ru.itis.application6.dependency_injection.ServiceLocator

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    private var viewBinding: FragmentAuthorizationBinding? = null
    private val containerID: Int = R.id.container
    private val userService = ServiceLocator.getUserService()
    private var sharedPref: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAuthorizationBinding.bind(view)

        sharedPref = (requireActivity() as? MainActivity)?.sharedPref

        viewBinding?.apply {

            btnAuthorization.setOnClickListener {
                val nickname = etNickname.text.toString()
                val password = etPassword.text.toString()

                lifecycleScope.launch {
                    val isUserVerified = userService.verifyUser(nickname, password)
                    if (isUserVerified) {
                        sharedPref?.edit {
                            putString(Properties.USER_NICK_SHARED_PREF, nickname)
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(containerID, MainFragment(), Properties.MAIN_FRAGMENT_TAG)
                            .commit()
                    } else {
                        showAlertDialogIncorrectData()
                    }
                }
            }

            btnRegistration.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(containerID, RegistrationFragment(), Properties.REGISTRATION_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    private fun showAlertDialogIncorrectData() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_dialog_incorrect_data_title)
            .setMessage(R.string.alert_dialog_incorrect_data_message)
            .setCancelable(true)
            .setPositiveButton(R.string.alert_dialog_incorrect_data_positive_button_text) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}