package ru.nifontbus.materialdesign.ui.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.data.PictureOfTheDayData
import ru.nifontbus.materialdesign.databinding.PhotoFragmentBinding
import ru.nifontbus.materialdesign.ui.picture.hide
import ru.nifontbus.materialdesign.ui.picture.show
import ru.nifontbus.materialdesign.ui.picture.toast
import java.time.LocalDate

class PhotoFragment(val photoDate: LocalDate): Fragment() {
    private var _binding: PhotoFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by lazy {
        ViewModelProvider(this).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.photoDate = photoDate
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Message link is empty")
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@PhotoFragment)
                        error(R.drawable.ic_load_error_vector)
//                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.tvDate.text = serverResponseData.date.toString()
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
        binding.includedLoadingLayout.loadingLayout.hide()
    }

    private fun showLoading() {
        binding.includedLoadingLayout.loadingLayout.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}