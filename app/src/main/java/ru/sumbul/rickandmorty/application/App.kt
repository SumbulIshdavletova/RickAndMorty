package ru.sumbul.rickandmorty.application

import android.app.Application
import android.content.Context
import ru.sumbul.rickandmorty.di.AppComponent
import ru.sumbul.rickandmorty.di.AppModule
import ru.sumbul.rickandmorty.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }