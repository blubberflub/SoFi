package com.blub.sofi.imgur.details

import com.blub.sofi.base.BaseViewModel
import com.blub.sofi.data.imgur.ImgurRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ImageDetailsViewModel
@Inject constructor(
    val repository: ImgurRepository,
    scheduler: Scheduler
) : BaseViewModel<Event, Result, ViewState>(scheduler) {
    override fun getInitialViewState(): ViewState {
        return ViewState.idle()
    }

    override fun handleEvent(event: Event): Observable<Result> {
        val observable = when (event) {
            is InitEvent -> handleInitEvent(event.imageResourceId)
        }

        return observable
    }

    private fun handleInitEvent(imageResourceId: String): Observable<Result> {
        val observable = repository
            .getImage(imageResourceId)
            .toObservable()
            .map<Result> { GetImageSuccess(it) }
            .onErrorReturn { Error }

        return observable
    }

    override fun reduce(): BiFunction<ViewState, Result, ViewState> {
        val biFunction = BiFunction { prevState: ViewState, result: Result ->
            when (result) {
                is GetImageSuccess -> prevState.copy(imageLink = result.link)
                Error -> prevState.copy(errorShown = true)
                Idle -> prevState.copy(errorShown = false)
                InFlight -> prevState.copy(progressShowing = true)
            }
        }

        return biFunction
    }
}