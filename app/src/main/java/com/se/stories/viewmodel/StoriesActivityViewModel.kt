package com.se.stories.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.module.ConnectivityModule
import com.se.stories.repository.IStoriesRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel for the StoriesActivity
 *
 * @see StoriesActivity
 */
class StoriesActivityViewModel(private val repository: IStoriesRepository,
                               private val connectivityModule: ConnectivityModule) : ViewModel() {

    val compositeDisposable by lazy { CompositeDisposable() }

    val storiesLiveData: MutableLiveData<LiveDataWrapper<List<StoryEntity>, Exception>> by lazy {
        MutableLiveData<LiveDataWrapper<List<StoryEntity>, Exception>>()
    }

    val availableOfflineLiveData: MutableLiveData<LiveDataWrapper<Boolean, Exception>> by lazy {
        MutableLiveData<LiveDataWrapper<Boolean, Exception>>()
    }

    var scrollPosition = 0

    /**
     * Method to fetch stories from the repository
     */
    fun fetchStories(forceRefresh: Boolean = false) {
        if (!forceRefresh && storiesLiveData.value != null
            && storiesLiveData.value?.status == ResourceStatus.SUCCESS) {
            availableOfflineLiveData.value = LiveDataWrapper(
                ResourceStatus.SUCCESS,
                false,
                null
            )
            return
        }

        storiesLiveData.value = LiveDataWrapper(
            ResourceStatus.LOADING,
            null,
            null
        )

        val networkAvailable = connectivityModule.isNetworkAvailable()
        val disposable = repository.getStories(!networkAvailable)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<StoryEntity>>() {
                override fun onSuccess(data: List<StoryEntity>) {
                    if (data.isNullOrEmpty()) {
                        storiesLiveData.value = LiveDataWrapper(
                            ResourceStatus.ERROR,
                            null,
                            Exception("Error fetching items")
                        )
                        return
                    }
                    storiesLiveData.value = LiveDataWrapper(
                        ResourceStatus.SUCCESS,
                        data,
                        null
                    )

                    if (networkAvailable) {
                        availableOfflineLiveData.value = LiveDataWrapper(
                            ResourceStatus.SUCCESS,
                            true,
                            null
                        )
                    }
                }

                override fun onError(e: Throwable) {
                    storiesLiveData.value = LiveDataWrapper(
                        ResourceStatus.ERROR,
                        null,
                        Exception(e.message)
                    )
                }

            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
