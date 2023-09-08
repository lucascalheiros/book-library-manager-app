package com.github.lucascalheiros.feature_splash.presentation.splash

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
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.feature_splash.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashFragment : Fragment() {

    private val googleSigInUseCase: GoogleSignInUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(SPLASH_VISUAL_DELAY)
            val navOptions = getSplashNavOptions()
            if (googleSigInUseCase.isUserSignedIn) {
                navOptions.goToHomeScreen()
            } else {
                navOptions.goToSignInScreen()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
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
    }
}