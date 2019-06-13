package com.blub.sofi.dagger

import com.blub.sofi.imgur.details.ImageDetailsFragment
import com.blub.sofi.imgur.list.view.ImageListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, NetModule::class])
interface AppComponent {
    fun inject(imageListFragment: ImageListFragment)

    fun inject(imageListFragment: ImageDetailsFragment)
}