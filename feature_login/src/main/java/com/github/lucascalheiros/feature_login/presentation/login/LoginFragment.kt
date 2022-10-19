package com.github.lucascalheiros.feature_login.presentation.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.R
import com.github.lucascalheiros.feature_login.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this

        loginViewModel.loginRequestState.observe(viewLifecycleOwner) {
            when (it) {
                is LoginRequestState.Success -> {
                    handleLoginSuccess()
                }
                is LoginRequestState.AskUser -> {
                    startForGoogleSignInResult.launch(it.signInIntent)
                }
                is LoginRequestState.Failure -> {
                    handleLoginFailure()
                }
                else -> {}
            }
        }

        return binding.root
    }

    private val startForGoogleSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(
                        result.data
                    ).getResult(ApiException::class.java)
                    loginViewModel.onLoginSuccess(account)
                } catch (exception: ApiException) {
                    loginViewModel.onLoginFailure(exception)
                }
            } else {
                loginViewModel.onLoginFailure(Exception("GoogleSignIn intent request returned: ${result.resultCode}"))
            }
        }

    private fun handleLoginSuccess() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(
                com.github.lucascalheiros.feature_login.R.id.loginFragment,
                true
            )
            .build()
        NavDeepLinkRequest.Builder
            .fromUri("android-app://com.github.lucascalheiros.booklibrarymanager/homeFragment".toUri())
            .build().let {
                findNavController().navigate(it, navOptions)
            }
    }

    private fun handleLoginFailure() {
        Toast.makeText(
            requireContext(),
            R.string.unable_to_sign_in_with_google_generic,
            Toast.LENGTH_LONG
        ).show()
    }

}