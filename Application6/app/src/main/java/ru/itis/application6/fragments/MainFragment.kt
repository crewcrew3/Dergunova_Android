package ru.itis.application6.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.launch
import ru.itis.application6.utils.Properties
import ru.itis.application6.R
import ru.itis.application6.activity.MainActivity
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.databinding.FragmentMainBinding
import ru.itis.application6.dependency_injection.ServiceLocator
import ru.itis.application6.recycler_view.SongAdapter

class MainFragment : Fragment(R.layout.fragment_main) {

    private var viewBinding: FragmentMainBinding? = null
    private var adapter: SongAdapter? = null
    private val containerID: Int = R.id.container
    private val userAndSongsService = ServiceLocator.getUserAndSongsService()
    private var sharedPref: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)

        sharedPref = (requireActivity() as? MainActivity)?.sharedPref

        val glide = Glide.with(this@MainFragment)
        val nickname = sharedPref?.getString(Properties.USER_NICK_SHARED_PREF, null)

        var songsList = emptyList<SongEntity>()
        lifecycleScope.launch {
            nickname?.let { safeNickname ->
                userAndSongsService.getUserAndTheirsSongs(safeNickname)?.let { safeModel ->
                    songsList = safeModel.songs
                }
            }
            initAdapter(glide, songsList)
        }

        viewBinding?.apply {

            btnLogOut.setOnClickListener {
                sharedPref?.edit {
                    remove(Properties.USER_NICK_SHARED_PREF)
                }
                parentFragmentManager.beginTransaction()
                    .replace(containerID, AuthorizationFragment(), Properties.AUTHORIZATION_FRAGMENT_TAG)
                    .commit()
            }

            fab.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(containerID, AddContentFragment(), Properties.ADD_CONTENT_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    private fun initAdapter(glide: RequestManager, items: List<SongEntity>) {
        viewBinding?.apply {
            adapter = SongAdapter(
                items = items,
                onLongClick = ::onLongClick,
                glide = glide,
            )
            rvContent.adapter = adapter
            rvContent.layoutManager = GridLayoutManager(
                requireContext(), 2, RecyclerView.VERTICAL, false
            )
        }
    }

    private fun onLongClick(song: SongEntity, adapterPosition: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_dialog_delete_title)
            .setMessage(R.string.alert_dialog_delete_message)
            .setCancelable(false)
            .setPositiveButton(R.string.alert_dialog_delete_positive_button_text) { _, _ ->
                adapter?.deleteItem(adapterPosition)
                lifecycleScope.launch {
                    userAndSongsService.deleteSong(song.userId, song.songId)
                }
            }
            .setNegativeButton(R.string.alert_dialog_delete_negative_button_text, null)
            .create()
            .show()
    }
}