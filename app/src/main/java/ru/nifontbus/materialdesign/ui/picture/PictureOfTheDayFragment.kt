package ru.nifontbus.materialdesign.ui.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.nifontbus.materialdesign.MainActivity
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.data.PictureOfTheDayData
import ru.nifontbus.materialdesign.databinding.MainFragmentBinding
import ru.nifontbus.materialdesign.ui.api.ApiActivity
import ru.nifontbus.materialdesign.ui.apibottom.ApiBottomActivity
import ru.nifontbus.materialdesign.ui.bottom.BottomNavigationDrawerFragment
import ru.nifontbus.materialdesign.ui.settings.SettingsFragment

class PictureOfTheDayFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                val req = binding.inputEditText.text.toString()
                data = Uri.parse("https://en.wikipedia.org/wiki/$req")
            })
        }
        setBottomAppBar(view)
//        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomSheetBehavior(binding.includedBottomSheet.bottomSheetContainer)

//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_DRAGGING -> TODO("not implemented")
//                    BottomSheetBehavior.STATE_COLLAPSED -> TODO("not implemented")
//                    BottomSheetBehavior.STATE_EXPANDED -> TODO("not implemented")
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> TODO("not implemented")
//                    BottomSheetBehavior.STATE_HIDDEN -> TODO("not implemented")
//                    BottomSheetBehavior.STATE_SETTLING -> TODO("not implemented")
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                TODO("Not yet implemented")
//            }
//        })
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
            R.id.app_bar_fav -> activity?.let { startActivity(Intent(it, ApiBottomActivity::class.java)) }
            R.id.app_bar_search -> Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings ->
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.container, SettingsFragment())
                    ?.addToBackStack(null)?.commit()

            R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }

            R.id.app_bar_api -> activity?.let { startActivity(Intent(it, ApiActivity::class.java))}

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
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

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
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

//                    binding.imageView.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png") {
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
//                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.includedBottomSheet.bottomSheetDescriptionHeader
                        .text = serverResponseData.title
                    binding.includedBottomSheet.bottomSheetDescription
                        .text = serverResponseData.explanation
                }
                hideLoading()
            }

            is PictureOfTheDayData.Loading -> {
                showLoading()
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun hideLoading() {
//        binding.imageView.show()
        binding.includedLoadingLayout.loadingLayout.hide()
    }

    private fun showLoading() {
//        binding.imageView.setImageIcon(null)
        binding.includedLoadingLayout.loadingLayout.show()
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
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
}