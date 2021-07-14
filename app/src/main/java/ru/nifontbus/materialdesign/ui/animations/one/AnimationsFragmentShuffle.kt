package ru.nifontbus.materialdesign.ui.animations.one

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.transition.*
import ru.nifontbus.materialdesign.databinding.FragmentAnimationsShuffleBinding

// Аннимация Shuffle
class AnimationsFragmentShuffle : Fragment() {

    private var _binding: FragmentAnimationsShuffleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationsShuffleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titles: MutableList<String> = ArrayList()
        for (i in 0..4) {
            titles.add(String.format("Item %d", i + 1))
        }
        binding.transitionsContainer.apply {
            createViews(this, titles)
            binding.button.setOnClickListener {
                TransitionManager.beginDelayedTransition(this, ChangeBounds())
                titles.shuffle()
                createViews(this, titles)
            }
        }

    }

    private fun createViews(layout: ViewGroup, titles: List<String>) {
        layout.removeAllViews()
        for (title in titles) {
            val textView = TextView(requireContext())
            textView.text = title
            textView.gravity = Gravity.CENTER_HORIZONTAL
            ViewCompat.setTransitionName(textView, title)
            layout.addView(textView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}