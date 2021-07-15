package ru.nifontbus.materialdesign.ui.animations.two

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import ru.nifontbus.materialdesign.R
import ru.nifontbus.materialdesign.databinding.FragmentAnimationsBonusStartBinding

class AnimationsFragmentBonus : Fragment() {

    private var _binding: FragmentAnimationsBonusStartBinding? = null
    private val binding get() = _binding!!
    private var show = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationsBonusStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backgroundImage.setOnClickListener {
            if (show) hideComponents() else showComponents()
        }
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_animations_bonus_end)

        val transition = ChangeBounds()

        // AnticipateOvershootInterpolator, как следует из названия,
        // позволяет добиться анимации отскока, когда анимированный элемент
        // сначала перепрыгивает конечную точку анимации, затем движется немного назад,
        // а затем вперёд — и замирает.
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_animations_bonus_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
