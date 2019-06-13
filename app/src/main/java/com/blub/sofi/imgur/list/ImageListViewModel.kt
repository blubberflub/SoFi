package com.blub.sofi.imgur.list

import com.blub.sofi.base.BaseViewModel
import com.blub.sofi.data.imgur.ImgurRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ImageListViewModel
@Inject constructor(
    val imgurRepository: ImgurRepository,
    scheduler: Scheduler
) : BaseViewModel<Event, Result, ViewState>(scheduler) {
    override fun getInitialViewState(): ViewState = ViewState.idle()

    override fun handleEvent(event: Event): Observable<Result> {
        val observable = when (event) {
            is SearchQueryChangedEvent -> handleSearchQueryChangedEvent(event.query)
        }

        return observable
    }

    private fun handleSearchQueryChangedEvent(query: String): Observable<Result> {
        val observable = imgurRepository
            .searchImages(1, query)
            .toObservable()
            .map<Result> {
                GetListSuccess(it)
            }
            .onErrorResumeNext(Observable.just(Error, Idle))
            .subscribeOn(scheduler)

        return observable
    }

    override fun reduce(): BiFunction<ViewState, Result, ViewState> {
        val biFunction = BiFunction { prevState: ViewState, result: Result ->
            val newState: ViewState = when (result) {
                is GetListSuccess -> prevState.copy(
                    imageList = result.imageList.toList(),
                    progressShown = false
                )
                Error -> prevState.copy(errorShown = true)
                Idle -> prevState.copy(errorShown = false)
                InFlight -> prevState.copy(progressShown = true)
            }

            newState
        }

        return biFunction
    }
}