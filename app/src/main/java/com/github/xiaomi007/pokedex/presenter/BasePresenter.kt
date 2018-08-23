package com.github.xiaomi007.pokedex.presenter

import android.arch.lifecycle.ViewModel
import android.support.annotation.MainThread
import android.util.Log
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * This [BasePresenter] aimed to centralized the loading and fetching event.
 *
 * @author Damien
 */
abstract class BasePresenter : ViewModel() {

    companion object {
        private const val TAG = "BasePresenter"
    }

    /**
     * @param isSuccessful true if the query has returned successfully (http 200), else false
     * @param error if the query has returned unsuccessfully, an error message will be added.
     */
    data class FetchStatus(val isSuccessful: Boolean = false, val error: Throwable? = null)

    /**
     * [mLoadingStatus] received 2 events: true when a query starts, false when a query finishes,
     * either successfully or unsuccessfully.
     */
    private val mLoadingStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()

    /**
     * [mFetchData] received 2 events:
     */
    private val mFetchData: BehaviorSubject<FetchStatus> = BehaviorSubject.create()

    /**
     * Register the flowable passed into [proceedToFetchData] and track its subscribe, error and complete
     * events.
     */
    private val proceedQueryCompletableObserver: CompletableObserver = object : CompletableObserver {

        override fun onSubscribe(d: Disposable) {
            Log.v(TAG, "Subscribe to proceed query")
            mDisposable.add(d)
        }

        override fun onError(e: Throwable) {
            Log.v(TAG, "Proceed query error $e")
            mFetchData.onNext(BasePresenter.FetchStatus(error = e))
        }

        override fun onComplete() {
            Log.v(TAG, "Proceed query completed")
            mFetchData.onNext(BasePresenter.FetchStatus(isSuccessful = true))
        }
    }

    /**
     * [mIsFetching] is used to not call a second time the api when the first call is still
     * processing.
     */
    private var mIsFetching = false

    /**
     * [mDisposable] collect the subscription of different observables and allow the use its
     * method [CompositeDisposable.clear] to unsubscribe them and therefore avoid observables leaks.
     */
    private var mDisposable = CompositeDisposable()

    /**
     * [proceedToFetchData] is an helper which takes a Flowable observable and add the logic to be
     * query once until it completes or fails.
     *
     * @param flowable observable to ob
     */
    fun <T> proceedToFetchData(flowable: Flowable<T>) {
        if (mIsFetching.not()) {
            flowable.compose(withLoading())
                    .doOnSubscribe { mIsFetching = true }
                    .doOnTerminate { mIsFetching = false }
                    .ignoreElements()
                    .subscribeWith(proceedQueryCompletableObserver)
        }
    }

    /**
     * [withLoading] method takes a flowable and add an event on subscription and after terminate
     * to emmit the loading status via [mLoadingStatus]
     *
     * @return a [FlowableTransformer]
     */
    private fun <T> withLoading(): FlowableTransformer<T, T> {
        return FlowableTransformer { ft ->
            ft.doOnSubscribe {
                Log.v(TAG, "Loading Starts")
                mLoadingStatus.onNext(true)
            }.doAfterTerminate {
                Log.v(TAG, "Loading Ends")
                mLoadingStatus.onNext(false)
            }
        }
    }

    /**
     * [observeLoading] method will emit true when the loading starts and false when the loading
     * stops.
     *
     * @return [Observable] of [Boolean].
     */
    fun observeLoading(): Observable<Boolean> {
        return mLoadingStatus.hide().observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * [observeQueryStatus] method will emit a [FetchStatus] object.
     * This object contains the status of the query, and if the query has failed, it will also
     * return a [Throwable]
     *
     * @return [Observable] of [FetchStatus]
     */
    @MainThread
    fun observeQueryStatus(): Observable<FetchStatus> {
        return mFetchData.hide().observeOn(AndroidSchedulers.mainThread())
    }


    override fun onCleared() {
        mLoadingStatus.onTerminateDetach()
        mFetchData.onTerminateDetach()
        mDisposable.clear()
        super.onCleared()
    }

}
