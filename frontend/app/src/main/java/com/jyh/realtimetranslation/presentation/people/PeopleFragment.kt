package com.jyh.realtimetranslation.presentation.people

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.animation.Animation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jyh.realtimetranslation.R
import com.jyh.realtimetranslation.databinding.FragmentPeopleBinding
import com.jyh.realtimetranslation.presentation.BaseFragment
import com.jyh.realtimetranslation.presentation.chatList.ChatListFragment
import com.jyh.realtimetranslation.presentation.offlinetranslation.OfflineTranslationActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


internal class PeopleFragment : BaseFragment<PeopleViewModel, FragmentPeopleBinding>() {

    companion object {
        const val TAG = "PeopleFragment"
    }

    override val viewModel by inject<PeopleViewModel>()

    override fun getViewBinding() = FragmentPeopleBinding.inflate(layoutInflater)

    // Fab 에니메이션 객체
    private val rotateOpen: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var isExpanded:Boolean = false

    // 로그인에 필요한 객체
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account->
                        lifecycleScope.launch { viewModel.saveToken(account.idToken ?: throw Exception()) }
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    // 데이터 관찰
    override fun observeData() {
        viewModel.loginStateLiveData.observe(this) {
            when (it) {
                is LoginState.Uninitialized -> initLoginViews()
                is LoginState.Loading -> handleLoginLoadingState()
                is LoginState.Login -> handleLoginState(it)
                is LoginState.Success -> handleLoginSuccessState(it)
                is LoginState.Error -> TODO()
            }
        }

        viewModel.peopleStateLiveData.observe(this) {
            when (it) {
//            is PeopleState.UnInitialized->{
//                initPeopleViews()
//            }
//            is PeopleState.Loading->{
//                handleLoadingState()
//            }
//            is PeopleState.Success->{
//                handleSuccessState(it)
//            }
//            is PeopleState.Error->{
//                handleErrorState()
//            }
            }
        }
    }

    // 화면 초기화
    private fun initPeopleViews() = with(binding) {
        //recyclerView.adapter = adapter
    }

    private fun initLoginViews() = with(binding) {
        requestPermissions()

        signInButton.setOnClickListener {
            googleSignIn()
        }

        fabButton.setOnClickListener {
            addButtonClicked()
        }

        speakFabButton.setOnClickListener {
            addButtonClicked()
            val intent = Intent(requireContext(), OfflineTranslationActivity::class.java)
            intent.putExtra("roomId", System.currentTimeMillis())
            intent.putExtra("isNew",true)
            startActivity(intent)
        }
    }

    // 로그인 State를 다루는 함수들
    private fun handleLoginLoadingState() = with(binding) {
        signInLayout.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    private fun handleLoginSuccessState(state: LoginState.Success) = with(binding) {
        progressbar.visibility = View.GONE
        when(state){
            is LoginState.Success.Registered ->{
                signInLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                chatFabButton.isEnabled = true
            }
            is LoginState.Success.NotRegistered ->{
                signInLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                chatFabButton.isEnabled = false
            }
        }
    }

    private fun handleLoginState(state: LoginState.Login) =with(binding) {
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    viewModel.setUserInfo(firebaseAuth.currentUser)
                } else {
                    viewModel.setUserInfo(null)
                }
            }
    }

    // 버튼에 들어갈 기능들
    private fun googleSignIn() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun addButtonClicked(){
        setVisibility()
        setAnimation()
        isExpanded = isExpanded.not()
    }

    private fun setAnimation() =with(binding){
        fabButton.startAnimation(if(isExpanded) rotateClose else rotateOpen )
        chatFabButton.startAnimation(if(isExpanded) toBottom else fromBottom )
        speakFabButton.startAnimation(if(isExpanded) toBottom else  fromBottom)
    }

    private fun setVisibility() =with(binding){
        chatFabButton.visibility= if(isExpanded) View.VISIBLE else View.GONE
        speakFabButton.visibility= if(isExpanded) View.VISIBLE else View.GONE
    }

    private fun requestPermissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.RECORD_AUDIO), 0)
        }
    }
}