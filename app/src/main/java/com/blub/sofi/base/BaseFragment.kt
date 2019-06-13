package com.blub.sofi.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blub.sofi.App
import com.blub.sofi.dagger.AppComponent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<E : BaseEvent, R : BaseResult, V : BaseViewState>(layoutId: Int) : Fragment(layoutId) {
    protected val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        inject(App.component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(
            getViewModel()
                .eventStream()
                .subscribe(::render)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }

    fun sendEvent(event: BaseEvent) {
        getViewModel().subject.onNext(event)
    }

    abstract fun render(viewState: V)

    abstract fun getViewModel(): BaseViewModel<E, R, V>

    abstract fun inject(component: AppComponent)
}
