package com.jyh.realtimetranslation.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jyh.realtimetranslation.R
import com.jyh.realtimetranslation.databinding.ActivityMainBinding
import com.jyh.realtimetranslation.presentation.BaseActivity
import com.jyh.realtimetranslation.presentation.chatList.ChatListFragment
import com.jyh.realtimetranslation.presentation.people.PeopleFragment
import com.jyh.realtimetranslation.presentation.settings.SettingsFragment
import org.koin.android.ext.android.inject

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by inject<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        showFragment(PeopleFragment(), PeopleFragment.TAG)
        initBottomNav(this)
    }

    private fun initBottomNav(binding: ActivityMainBinding) {
        binding.bottomNav.setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.bottom_people -> {
                    showFragment(PeopleFragment(), PeopleFragment.TAG)
                    true
                }
                R.id.bottom_list -> {
                    showFragment(ChatListFragment(), ChatListFragment.TAG)
                    true
                }
                R.id.bottom_settings -> {
                    showFragment(SettingsFragment(), SettingsFragment.TAG)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    override fun observeData() = viewModel.loginStateLiveData.observe(this){
        login(it)
    }

    private fun login(signed:Boolean) = with(binding){
        if(signed && bottomNav.menu.findItem(R.id.bottom_people).isChecked){
            showFragment(PeopleFragment(), PeopleFragment.TAG)
        }
    }
}