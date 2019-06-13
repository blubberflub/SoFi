package com.blub.sofi.dagger

import com.blub.sofi.BuildConfig
import com.blub.sofi.extensions.runIf
import com.blub.sofi.utils.AuthInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetModule {
    @Provides
    @Singleton
    fun providesRetro(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        val baseUrl = "https://api.imgur.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit
    }

    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .runIf(BuildConfig.DEBUG) {
                val httpInterceptor = HttpLoggingInterceptor()
                httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

                addInterceptor(httpInterceptor)
            }
            .build()

        return okHttpClient
    }

    @Provides
    fun providesMoshi(): Moshi {
        return Moshi
            .Builder()
            .build()
    }

    @Provides
    fun providesScheduler(): Scheduler = Schedulers.io()
}