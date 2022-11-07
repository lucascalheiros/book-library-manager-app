package com.github.lucascalheiros.feature_splash.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.data_authentication.useCase.GoogleSignInUseCase
import com.github.lucascalheiros.feature_splash.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashFragment : Fragment() {

    private val googleSigInUseCase: GoogleSignInUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch {
            delay(500)
             val navOptions = NavOptions.Builder()
                .setPopUpTo(
                    R.id.splashFragment,
                    true
                )
                .build()
            if (googleSigInUseCase.isUserSignedIn) {
                NavDeepLinkRequest.Builder
                    .fromUri(NavigationRoutes.homeScreen)
                    .build().let {
                        findNavController().navigate(it, navOptions)
                    }
            } else {
                NavDeepLinkRequest.Builder
                    .fromUri(NavigationRoutes.loginScreen)
                    .build().let {
                        findNavController().navigate(it, navOptions)
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}