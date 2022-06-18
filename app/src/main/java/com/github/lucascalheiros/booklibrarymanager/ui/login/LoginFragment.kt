package com.github.lucascalheiros.booklibrarymanager.ui.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentLoginBinding
import com.github.lucascalheiros.booklibrarymanager.useCase.GoogleSignInUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private val googleSignInUseCase: GoogleSignInUseCase by inject()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener {
            onClickGoogleSignIn()
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
                    logAccount(account)
                    val action = LoginFragmentDirections.actionLoginFragmentToPdfReaderFragment()
                    findNavController().navigate(action)
                } catch (exception: ApiException) {
                    exception.printStackTrace()
                }
            }
        }

    private fun onClickGoogleSignIn() {
        val signInIntent = googleSignInUseCase.googleSignInClient.signInIntent
        startForGoogleSignInResult.launch(signInIntent)
    }

    private fun logAccount(account: GoogleSignInAccount) {
        Log.d(
            "[LOGIN]", "Login successful with account\n" +
                    "ID: ${account.id}\n" +
                    "Name: ${account.displayName}\n" +
                    "ServerAuth: ${account.serverAuthCode}\n" +
                    "ID Token: ${account.idToken}\n" +
                    "Email: ${account.email}\n" +
                    "PhotoUri: ${account.photoUrl}\n"
        )
    }
}