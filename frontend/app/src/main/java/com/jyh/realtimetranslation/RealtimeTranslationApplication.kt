package com.jyh.realtimetranslation

import android.app.Application
import com.jyh.realtimetranslation.data.room.AppDatabase
import com.jyh.realtimetranslation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class RealtimeTranslationApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RealtimeTranslationApplication)
            modules(appModule)
        }
    }
}