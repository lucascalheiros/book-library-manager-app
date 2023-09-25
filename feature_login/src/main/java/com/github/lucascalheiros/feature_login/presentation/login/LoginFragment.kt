package com.github.lucascalheiros.feature_login.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.R
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.GoogleSignInConfiguration
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

        loginViewModel.loginRequestState.observe(viewLifecycleOwner, this::handleLoginStateChange)

        return binding.root
    }

    private val startForGoogleSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                onSuccessfulSignInResult(result)
            } else {
                onFailedSignInResult(result)
            }
        }

    private fun onSuccessfulSignInResult(result: ActivityResult) {
        try {
            GoogleSignIn.getSignedInAccountFromIntent(
                result.data
            ).getResult(ApiException::class.java)
            loginViewModel.onLoginSuccess()
        } catch (exception: ApiException) {
            logError(
                listOf(LogTags.LOGIN, TAG),
                "GoogleSignIn intent request returned: ${result.resultCode}",
            )
            loginViewModel.onLoginFailure()
        }
    }

    private fun onFailedSignInResult(result: ActivityResult) {
        logError(
            listOf(LogTags.LOGIN, TAG),
            "GoogleSignIn intent request returned: ${result.resultCode}",
        )
        loginViewModel.onLoginFailure()
    }

    private fun askUserGoogleLoginCredentials() {
        startForGoogleSignInResult.launch(getGoogleSignInIntent())
    }

    private fun handleLoginStateChange(state: LoginRequestState) {
        when (state) {
            is LoginRequestState.Success -> {
                onLoginSuccess()
            }
            is LoginRequestState.AskUser -> {
                askUserGoogleLoginCredentials()
            }
            is LoginRequestState.Failure -> {
                onLoginFailure()
            }
            else -> {}
        }
    }

    private fun onLoginSuccess() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(
                com.github.lucascalheiros.feature_login.R.id.loginFragment,
                true
            )
            .build()
        NavDeepLinkRequest.Builder
            .fromUri(NavigationRoutes.homeScreen)
            .build().run {
                findNavController().navigate(this, navOptions)
            }
    }

    private fun onLoginFailure() {
        Toast.makeText(
            requireContext(),
            R.string.unable_to_sign_in_with_google_generic,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getGoogleSignInIntent(): Intent {
        return GoogleSignIn.getClient(requireContext(), GoogleSignInConfiguration.googleSignInOptions).signInIntent
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}