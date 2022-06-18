package com.github.lucascalheiros.booklibrarymanager.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.booklibrarymanager.R
import com.github.lucascalheiros.booklibrarymanager.useCase.GoogleSignInUseCase
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
            if (googleSigInUseCase.isUserSignedIn()) {
                SplashFragmentDirections.actionSplashFragmentToHomeFragment().let {
                    findNavController().navigate(it)
                }
            } else {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment().let {
                    findNavController().navigate(it)
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