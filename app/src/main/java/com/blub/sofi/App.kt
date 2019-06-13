package com.blub.sofi

import android.app.Application
import com.blub.sofi.dagger.AppComponent
import com.blub.sofi.dagger.NetModule
import com.blub.sofi.dagger.DaggerAppComponent

class App : Application() {
    companion object {
        @JvmStatic
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
            .builder()
            .netModule(NetModule())
            .build()
    }
}

