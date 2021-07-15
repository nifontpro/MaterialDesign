package ru.nifontbus.materialdesign.ui.animations.one

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.transition.*
import ru.nifontbus.materialdesign.databinding.FragmentAnimationsEnlargeBinding
import ru.nifontbus.materialdesign.databinding.FragmentAnimationsPathTransitionsBinding

// Аннимация по плавной траетории
class AnimationsFragmentPath : Fragment() {

    private var _binding: FragmentAnimationsPathTransitionsBinding? = null
    private val binding get() = _binding!!
    private var toRightAnimation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationsPathTransitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            // Класс ChangeBounds принадлежит к пакету анимаций, как и ArcMotion.
            // Он предназначен для отображения анимации перемещения по плавным траекториям.
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 500
            TransitionManager.beginDelayedTransition(
                binding.root,
                changeBounds
            )

            toRightAnimation = !toRightAnimation
            val params = binding.button.layoutParams as FrameLayout.LayoutParams
            params.gravity =
                if (toRightAnimation) Gravity.END or Gravity.BOTTOM else Gravity.START or Gravity.TOP
            binding.button.layoutParams = params
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}