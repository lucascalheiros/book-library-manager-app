package com.github.lucascalheiros.feature_account.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.feature_account.R
import com.github.lucascalheiros.feature_account.databinding.FragmentAccountBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private val accountViewModel: AccountViewModel by viewModel()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountViewModel.logoutEvent.observe(this) {
            when (it) {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = accountViewModel

        return binding.root
    }


}