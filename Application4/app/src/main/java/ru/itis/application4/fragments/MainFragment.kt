package ru.itis.application4.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import ru.itis.application4.recyclerViewTheme.Theme
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.itis.application4.MainActivity
import ru.itis.application4.Properties
import ru.itis.application4.R
import ru.itis.application4.databinding.FragmentMainBinding
import ru.itis.application4.notifications.ChannelConstants
import ru.itis.application4.notifications.NotificationType
import ru.itis.application4.notifications.models.NotificationData
import ru.itis.application4.recyclerViewTheme.ThemeAdapter
import ru.itis.application4.recyclerViewTheme.ThemeData
import ru.itis.application4.utils.NotificationHandler
import ru.itis.application4.utils.PermissionsHandler

class MainFragment : Fragment(R.layout.fragment_main) {

    private var viewBinding: FragmentMainBinding? = null
    private var adapter: ThemeAdapter? = null
    private var notificationHandler: NotificationHandler? = null
    private var pickImageResultLauncher: ActivityResultLauncher<Intent>? = null
    private var notificationIDCounter = 0
    private var isImageLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (notificationHandler == null) {
            notificationHandler = (requireActivity() as? MainActivity)?.notificationHandler
        }

        viewBinding = FragmentMainBinding.bind(view)

        initAdapter(requireActivity().applicationContext)

        viewBinding?.apply {
            //Даем спиннеру список айдишников каналов разной важности
            val notificationsChannelsIDs = ChannelConstants.notificationsChannelsData.map{it.id}
            //Создаем ArrayAdapter(дефолтный адаптер для связывания списка(в нашем случае это список строк) данных с пользовательским интерфейсом) и используем дефолтную верстку для элемента списка.
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                notificationsChannelsIDs,
            ).also { adapter ->
                // Тут тоже дефолтная верстка для раскрытого списка(?)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                importanceSpinner.adapter = adapter
            }

            var selectedNotificationType: NotificationType? = null
            importanceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Определяем тип уведомления на основе позиции
                    selectedNotificationType = when (position) {
                        0 -> NotificationType.MAX
                        1 -> NotificationType.HIGH
                        2 -> NotificationType.DEFAULT
                        3 -> NotificationType.LOW
                        else -> null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedNotificationType = null
                }
            }

            //начиная с 13 андроида используется READ_MEDIA_IMAGES
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                android.Manifest.permission.READ_MEDIA_IMAGES
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }

            val permissionHandler = PermissionsHandler(
                mapOf(permission to ::galleryPermissionGranted)
            )
            permissionHandler.initLaunchers(activity = (requireActivity() as MainActivity))

            pickImageResultLauncher = registerForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                callback = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            loadImage(uri)
                        }
                    }
                }
            )

            ivImageContainer.setOnClickListener {
                if (!isImageLoaded) {
                    val grantedCode =
                        ContextCompat.checkSelfPermission(requireActivity(), permission)
                    if (grantedCode != PackageManager.PERMISSION_GRANTED) {
                        permissionHandler.requestMultiplePermission(listOf(permission))
                    } else {
                        galleryPermissionGranted()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.toast_image_has_already_loaded, Toast.LENGTH_SHORT).show()
                }
            }

            ivDelete.setOnClickListener{
                ivImage.setImageResource(R.drawable.pic_circle)
                ivDelete.setVisibility(View.INVISIBLE)
                isImageLoaded = false
            }

            var isRecyclerViewVisible = false
            btnTheme.setOnClickListener {
                isRecyclerViewVisible = !isRecyclerViewVisible
                rvThemes.visibility = if (isRecyclerViewVisible) View.VISIBLE else View.GONE
                btnTheme.setImageResource(
                    if (isRecyclerViewVisible) R.drawable.ic_double_arrow_up else R.drawable.ic_double_arrow_down
                )
            }

            btnShowNotification.setOnClickListener {
                val notificationTitle = etTitle.text
                val notificationMessage = etMessage.text

                when {

                    notificationTitle.isNullOrBlank() && notificationMessage.isNullOrBlank() -> {
                        Toast.makeText(requireContext(), R.string.toast_empty_notification_all, Toast.LENGTH_SHORT).show()
                    }

                    notificationTitle.isNullOrBlank() -> {
                        Toast.makeText(requireContext(), R.string.toast_empty_notification_title, Toast.LENGTH_SHORT).show()
                    }

                    notificationMessage.isNullOrBlank() -> {
                        Toast.makeText(requireContext(), R.string.toast_empty_notification_message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        notificationHandler?.showNotification(
                            notificationData = NotificationData(
                                id = ++notificationIDCounter,
                                title = notificationTitle.toString(),
                                message = notificationMessage.toString(),
                                notificationType = selectedNotificationType
                            )
                        )
                    }
                }
            }

            btnSetDefaultTheme.setOnClickListener {
                (requireActivity() as? MainActivity)?.changeTheme(Properties.THEME_ID_DEFAULT)
            }
        }
    }

    override fun onDestroy() {
        notificationHandler = null
        viewBinding = null
        super.onDestroy()
    }

    private fun initAdapter(context: Context) {
        viewBinding?.apply {
            adapter = ThemeAdapter(
                items = ThemeData.getThemeList(context),
                onClick = ::onClick,
            )
            rvThemes.adapter = adapter
            rvThemes.layoutManager = LinearLayoutManager (
                requireContext(), RecyclerView.HORIZONTAL, false
            )
        }
    }

    private fun onClick(theme: Theme) {
        (requireActivity() as? MainActivity)?.changeTheme(theme.id)
    }

    private fun galleryPermissionGranted() {
        /* Создаем интент для открытия галереи. ACTION_PICK указывает на то, что мы хотим выбрать данные,
        а MediaStore.Images.Media.EXTERNAL_CONTENT_URI - это URI контента, представляющий все изображения
        на внешнем хранилище устройства.*/
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageResultLauncher?.launch(intent)
    }

    private fun loadImage(uri: Uri) {
        viewBinding?.apply {
            Glide.with(this@MainFragment)
                .load(uri)
                .circleCrop()
                .into(ivImage)
            ivDelete.setVisibility(View.VISIBLE)
            isImageLoaded = true
        }
    }
}