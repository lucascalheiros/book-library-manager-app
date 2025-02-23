package com.github.lucascalheiros.feature_account.presentation.account

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.GoogleSignInConfiguration
import com.github.lucascalheiros.feature_account.R
import com.github.lucascalheiros.feature_account.databinding.FragmentAccountBinding
import com.github.service_synchronization.worker.FileDownloadWorker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private val accountViewModel: AccountViewModel by viewModel()
    private var binding: FragmentAccountBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountViewModel.updateAccountInfo()
        return FragmentAccountBinding.inflate(inflater, container, false).also {
            binding = it
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = accountViewModel

            it.downloadDataBtn.setOnClickListener {
                FileDownloadWorker.startWorker(it.context)
            }
        }.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun observeViewModel() {
        accountViewModel.logoutEvent.observe(this, this::handleLogoutRequest)
        lifecycleScope.launch {
            accountViewModel.linkWithGoogleEvent.collect {
                startForGoogleSignInResult.launch(getGoogleSignInIntent())
            }
        }
    }

    private fun handleLogoutRequest(logoutRequestState: LogoutRequestState) {
        when (logoutRequestState) {
            is LogoutRequestState.Success -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(
                        R.id.accountFragment,
                        true
                    )
                    .build()
                NavDeepLinkRequest.Builder
                    .fromUri(NavigationRoutes.loginScreen)
                    .build().let {
                        findNavController().navigate(it, navOptions)
                    }
            }
            else -> {}
        }
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
            accountViewModel.onLinkWithGoogleSuccess()
        } catch (exception: ApiException) {
            logError(
                GOOGLE_SIGN_IN_TAGS,
                "::onSuccessfulSignInResult",
                exception
            )
            onFailedSignInResult(result)
        }
    }

    private fun onFailedSignInResult(result: ActivityResult) {
        logError(
            GOOGLE_SIGN_IN_TAGS,
            "::onFailedSignInResult GoogleSignIn intent request returned: ${result.resultCode}",
        )
        onLinkWithGoogleFailure()
    }

    private fun onLinkWithGoogleFailure() {
        Toast.makeText(
            requireContext(),
            com.github.lucascalheiros.common.R.string.unable_to_link_with_google_generic,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getGoogleSignInIntent(): Intent {
        return GoogleSignIn.getClient(requireContext(), GoogleSignInConfiguration.googleSignInOptions).signInIntent
    }

    companion object {
        private val TAG = AccountFragment::class.java.simpleName
        private val GOOGLE_SIGN_IN_TAGS = listOf(LogTags.LOGIN, LogTags.GOOGLE, TAG)
    }

}