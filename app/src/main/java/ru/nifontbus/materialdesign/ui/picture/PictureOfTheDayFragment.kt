package ru.nifontbus.materialdesign.ui.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import ru.nifontbus.materialdesign.MainActivity
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.data.PictureOfTheDayData
import ru.nifontbus.materialdesign.data.StateFragment
import ru.nifontbus.materialdesign.databinding.FragmentMainBinding
import ru.nifontbus.materialdesign.ui.bottom.BottomNavigationDrawerFragment
import ru.nifontbus.materialdesign.ui.recycler.notes.Note
import ru.nifontbus.materialdesign.ui.recycler.notes.NotesFragment
import ru.nifontbus.materialdesign.ui.settings.SettingsFragment
import ru.nifontbus.materialdesign.ui.view_pager.MainPhotoFragment
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

interface OnSetDateInMainFragment {
    fun setDate(date: LocalDate)
}

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }
    private var isExpanded = false
    private var currentNote = Note("Пустая")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                val req = binding.inputEditText.text.toString()
                data = Uri.parse("https://en.wikipedia.org/wiki/$req")
            })
        }
        setBottomAppBar()
        setBottomSheetBehavior(binding.includedBottomSheet.bottomSheetContainer)
        setDatePick()
        setImageClick()

        viewModel.sendServerRequest()
    }

    private fun setImageClick() {
        binding.imageView.setOnClickListener {

            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                binding.root, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            binding.imageView.layoutParams = params
            binding.imageView.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }


    private fun setDatePick() {
        binding.btnChangeDate.setOnClickListener {
            val zoneId = ZoneId.systemDefault()
            val localDateInMilli =
                viewModel.liveData.value!!.date.atStartOfDay(zoneId).toEpochSecond() * 1000
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(localDateInMilli)
                .build()
            datePicker.show(childFragmentManager, "tag")

            datePicker.addOnPositiveButtonClickListener {
                // convert - https://howtoprogram.xyz/2017/02/11/convert-milliseconds-localdatetime-java/
                val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                viewModel.setDate(date)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> BottomNavigationDrawerFragment().show(childFragmentManager, "tag")
//            R.id.app_bar_fav -> replaceFragment(ApiBottomFragment())
            R.id.app_bar_fav -> replaceFragment(
                NotesFragment(currentNote,
                    object : OnSetDateInMainFragment {
                        override fun setDate(date: LocalDate) {
                            requireActivity().supportFragmentManager.popBackStack()
                            viewModel.setDate(date)
                        }
                    })
            )
            R.id.app_bar_api -> replaceFragment(MainPhotoFragment())
            R.id.app_bar_settings -> replaceFragment(SettingsFragment())
            R.id.app_bar_search -> Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun renderData(state: StateFragment) {
        when (val data = state.photo) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Отобразите ошибку
                    toast("Message link is empty")
                } else {
                    //Отобразите фото
                    //showSuccess()
                    //Coil в работе: достаточно вызвать у нашего ImageView
                    //нужную extension-функцию и передать ссылку и заглушки для placeholder

                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
//                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    currentNote.title = serverResponseData.title.toString()
                    binding.includedBottomSheet.bottomSheetDescriptionHeader
                        .text = currentNote.title
                    currentNote.description = serverResponseData.explanation.toString()
                    binding.includedBottomSheet.bottomSheetDescription
                        .text = currentNote.description

                    val dateString = serverResponseData.date
                    currentNote.date = LocalDate.parse(dateString)
                    binding.tvDate.text = dateString
                }
                hideLoading()
            }

            is PictureOfTheDayData.Loading -> {
                showLoading()
            }
            is PictureOfTheDayData.Error -> {
                hideLoading()
                toast(data.error.message)
            }
        }
    }

    private fun hideLoading() {
        binding.includedLoadingLayout.loadingLayout.hide()
    }

    private fun showLoading() {
        binding.includedLoadingLayout.loadingLayout.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}

// Управлять видимостью View:
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}

fun Fragment.replaceFragment(fragment: Fragment) {
    activity?.supportFragmentManager
        ?.beginTransaction()
        ?.replace(R.id.container, fragment)
        ?.addToBackStack(null)
        ?.commit()
}