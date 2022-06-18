package com.github.lucascalheiros.booklibrarymanager.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.booklibrarymanager.R
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentLoginBinding
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
                    startForGoogleSignInResult.launch(it.client.signInIntent)
                }
                is LoginRequestState.Failure -> {
                    handleLoginFailure()
                }
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
                loginViewModel.onLoginFailure()
            }
        }

    private fun handleLoginSuccess() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun handleLoginFailure() {
        Toast.makeText(requireContext(), R.string.unable_to_login_with_google_generic, 2000).show()
    }

}