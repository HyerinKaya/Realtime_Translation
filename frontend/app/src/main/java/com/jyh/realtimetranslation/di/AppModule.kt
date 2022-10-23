package com.jyh.realtimetranslation.di

import com.jyh.realtimetranslation.data.network.provideAuth
import com.jyh.realtimetranslation.data.network.provideUsersDB
import com.jyh.realtimetranslation.data.repository.PeopleRepository
import com.jyh.realtimetranslation.data.repository.PeopleRepositoryImpl
import com.jyh.realtimetranslation.domain.GetPeopleUseCase
import com.jyh.realtimetranslation.presentation.chatList.ChatListViewModel
import com.jyh.realtimetranslation.presentation.main.MainViewModel
import com.jyh.realtimetranslation.presentation.offlinetranslation.OfflineTranslationViewModel
import com.jyh.realtimetranslation.presentation.people.PeopleViewModel
import com.jyh.realtimetranslation.presentation.settings.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // viewModel
    viewModel { MainViewModel() }
    viewModel { ChatListViewModel() }
    viewModel { PeopleViewModel(get(), androidContext()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { OfflineTranslationViewModel(androidContext()) }

    // Coroutines Dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }


    factory { GetPeopleUseCase(get()) }


    // Repositories
    single { provideAuth() }
    single { provideUsersDB() }
    single<PeopleRepository> { PeopleRepositoryImpl(get(), get()) }
}