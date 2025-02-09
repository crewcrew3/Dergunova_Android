package ru.itis.application6.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.itis.application6.R
import ru.itis.application6.activity.MainActivity
import ru.itis.application6.databinding.FragmentAddContentBinding
import ru.itis.application6.dependency_injection.ServiceLocator
import ru.itis.application6.utils.Properties

class AddContentFragment : Fragment(R.layout.fragment_add_content) {

    private var viewBinding: FragmentAddContentBinding? = null
    private val containerID: Int = R.id.container
    private val songService = ServiceLocator.getSongService()
    private val userService = ServiceLocator.getUserService()
    private var sharedPref: SharedPreferences? = null

    private var pickImageResultLauncher: ActivityResultLauncher<Intent>? = null
    private var posterUrl: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAddContentBinding.bind(view)

        sharedPref = (requireActivity() as? MainActivity)?.sharedPref
        val permissionsHandler = (requireActivity() as? MainActivity)?.permissionHandler

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        permissionsHandler?.onMultiplePermissionGranted =
            mapOf(permission to ::galleryPermissionGranted)

        pickImageResultLauncher = registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            callback = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        posterUrl = uri
                        loadImage(uri)
                    }
                }
            }
        )

        viewBinding?.apply {
            ivPoster.setOnClickListener {
                val grantedCode =
                    ContextCompat.checkSelfPermission(requireActivity(), permission)
                if (grantedCode != PackageManager.PERMISSION_GRANTED) {
                    permissionsHandler?.requestMultiplePermission(listOf(permission))
                } else {
                    galleryPermissionGranted()
                }
            }

            btnAddNewItem.setOnClickListener {
                //val posterUrl = etSongPoster.text.toString().takeIf {it.isNotBlank()}
                val name = etSongName.text.toString()
                val author = etSongAuthor.text.toString().takeIf { it.isNotBlank() }
                val year = etSongYear.text.toString().toIntOrNull()
                val lyrics = etSongLyrics.text.toString().takeIf { it.isNotBlank() }

                lifecycleScope.launch {
                    val currentUserNickname = sharedPref?.getString(Properties.USER_NICK_SHARED_PREF, null)
                    val currentUserID = userService.getUserIDByNickname(currentUserNickname)
                    currentUserID?.let { userID ->
                        //если добавили корректную песню, то навигируемся на главный экран
                        if (songService.saveSong(
                                userId = userID,
                                name = name,
                                author = author,
                                year = year,
                                lyrics = lyrics,
                                posterUrl = posterUrl
                            )
                        ) {
                            parentFragmentManager.beginTransaction()
                                .replace(containerID, MainFragment(), Properties.MAIN_FRAGMENT_TAG)
                                .commit()
                        } else {
                            showAlertDialogIncorrectData()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    private fun galleryPermissionGranted() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageResultLauncher?.launch(intent)
    }

    private fun loadImage(uri: Uri) {
        viewBinding?.apply {
            Glide.with(this@AddContentFragment)
                .load(uri)
                .placeholder(R.drawable.pic_empty)
                .into(ivPoster)
        }
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