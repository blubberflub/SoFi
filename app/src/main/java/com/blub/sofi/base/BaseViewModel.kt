package com.blub.sofi.base

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<E : BaseEvent, R : BaseResult, V : BaseViewState>
constructor(val scheduler: Scheduler) : ViewModel() {
    val subject: PublishSubject<E> = PublishSubject.create()
    private val viewStateObservable: Observable<V> = eventStream()

    fun eventStream(): Observable<V> {
        return subject
            .subscribeOn(scheduler)
            .flatMap(::handleEvent)
            .scan(getInitialViewState(), reduce())
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
            .observeOn(AndroidSchedulers.mainThread())
    }

    abstract fun getInitialViewState(): V

    abstract fun handleEvent(event: E): Observable<R>

    abstract fun reduce(): BiFunction<V, R, V>
}