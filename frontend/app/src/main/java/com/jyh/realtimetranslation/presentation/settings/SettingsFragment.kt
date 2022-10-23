package com.jyh.realtimetranslation.presentation.settings

import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.jyh.realtimetranslation.R
import com.jyh.realtimetranslation.databinding.FragmentSettingsBinding
import com.jyh.realtimetranslation.presentation.BaseFragment
import com.jyh.realtimetranslation.presentation.main.MainActivity
import com.jyh.realtimetranslation.presentation.people.PeopleViewModel
import org.koin.android.ext.android.inject

internal class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val viewModel by inject<SettingsViewModel>()

    companion object {
        const val TAG = "SettingsFragment"
    }

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.loginStateLiveData.observe(this){
        initViews(it)
    }

    private fun initViews(isSigned:Boolean) = with(binding){
        logoutButton.isEnabled = isSigned
        logoutButton.setOnClickListener {
            auth.signOut()
            viewModel.signOut()
            gsc.signOut()
        }
    }
}