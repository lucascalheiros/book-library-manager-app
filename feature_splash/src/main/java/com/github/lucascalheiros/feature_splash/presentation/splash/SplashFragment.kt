package com.github.lucascalheiros.feature_splash.presentation.splash

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase
import com.github.lucascalheiros.feature_splash.R
import com.github.lucascalheiros.feature_splash.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashFragment : Fragment() {

    private val signedAccountUseCase: SignedAccountUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        animateLoadingIndicator(binding.ivLogo)
        navigateAfterDelay()
        return binding.root
    }

    private fun navigateAfterDelay() {
        lifecycleScope.launch {
            delay(SPLASH_VISUAL_DELAY)
            val navOptions = getSplashNavOptions()
            if (signedAccountUseCase.isUserSignedIn()) {
                navOptions.goToHomeScreen()
            } else {
                navOptions.goToSignInScreen()
            }
        }
    }

    private fun animateLoadingIndicator(view: View) {
        val loadingAnimation = ValueAnimator.ofFloat(-LOADING_ANIMATION_ROTATION, LOADING_ANIMATION_ROTATION)
        loadingAnimation.addUpdateListener { animation ->
            view.rotation = animation.animatedValue as Float
            view.requestLayout()
        }
        loadingAnimation.repeatCount = INFINITE
        loadingAnimation.duration = LOADING_ANIMATION_TIME
        loadingAnimation.repeatMode = REVERSE
        loadingAnimation.start()
    }

    private fun getSplashNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setPopUpTo(
                R.id.splashFragment,
                true
            )
            .build()
    }

    private fun NavOptions.goToHomeScreen() {
        NavDeepLinkRequest.Builder
            .fromUri(NavigationRoutes.homeScreen)
            .build().let {
                findNavController().navigate(it, this)
            }
    }

    private fun NavOptions.goToSignInScreen() {
        NavDeepLinkRequest.Builder
            .fromUri(NavigationRoutes.loginScreen)
            .build().let {
                findNavController().navigate(it, this)
            }
    }

    companion object {
        private const val SPLASH_VISUAL_DELAY = 500L
        private const val LOADING_ANIMATION_TIME = 200L
        private const val LOADING_ANIMATION_ROTATION = 20f

    }
}