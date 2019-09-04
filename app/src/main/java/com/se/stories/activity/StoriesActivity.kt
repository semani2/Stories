package com.se.stories.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.test.espresso.idling.CountingIdlingResource
import com.se.stories.R
import com.se.stories.adapter.StoryAdapter
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.module.ConnectivityModule
import com.se.stories.viewmodel.LiveDataWrapper
import com.se.stories.viewmodel.ResourceStatus
import com.se.stories.viewmodel.StoriesActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_stories.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoriesActivity : AppCompatActivity() {

    val viewmodel: StoriesActivityViewModel by viewModel()

    private val storiesList = mutableListOf<StoryEntity>()
    private val storyAdapter = StoryAdapter(storiesList)

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val connectivityModule: ConnectivityModule by inject()

    val countingIdlingResource = CountingIdlingResource("async_operations")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        initRecyclerView()
        initSwipeToRefresh()
        initStoryItemClick()
        initLiveDataObservers()

        fetchStories()
    }

    override fun onDestroy() {
        viewmodel.scrollPosition = (stories_recycler_view.layoutManager as GridLayoutManager)
            .findFirstCompletelyVisibleItemPosition()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    /* Section - Data */

    /**
     * Method to initialize the ViewModels' live data observers
     */
    private fun initLiveDataObservers() {
        viewmodel.storiesLiveData.observe(this,
            Observer<LiveDataWrapper<List<StoryEntity>, Exception>> { livedataWrapper ->
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> toggleBusy(true)

                    ResourceStatus.ERROR -> {
                        swipe_refresh_layout.isRefreshing = false
                        toggleBusy(false)
                        Timber.e(livedataWrapper.exception, "Error fetching items")

                        displayItemsError()
                        countingIdlingResource.decrement()
                    }

                    ResourceStatus.SUCCESS -> {
                        swipe_refresh_layout.isRefreshing = false
                        toggleBusy(false)

                        if (livedataWrapper.data.isNullOrEmpty()) {
                            displayItemsError()
                            return@Observer
                        }

                        storiesList.clear()
                        storiesList.addAll(livedataWrapper.data)
                        storyAdapter.notifyDataSetChanged()

                        stories_recycler_view.visibility = View.VISIBLE
                        empty_list_text_view.visibility = View.GONE

                        stories_recycler_view.smoothScrollToPosition(viewmodel.scrollPosition)
                        countingIdlingResource.decrement()
                    }
                }
        })

        viewmodel.availableOfflineLiveData.observe(this,
            Observer<LiveDataWrapper<Boolean, Exception>> { livedataWrapper ->
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> {}

                    ResourceStatus.ERROR -> {}

                    ResourceStatus.SUCCESS -> {
                        if (livedataWrapper.data != null && livedataWrapper.data) {
                            Toast.makeText(this, getString(R.string.str_available_offline),
                                Toast.LENGTH_LONG).show()
                            viewmodel.availableOfflineLiveData.value = LiveDataWrapper(
                                ResourceStatus.SUCCESS,
                                false,
                                null
                            )
                        }
                    }
                }
        })
    }

    private fun displayItemsError() {
        stories_recycler_view.visibility = View.GONE
        if (!connectivityModule.isNetworkAvailable()) {
            empty_list_text_view.text = getString(R.string.str_empty_list_no_network)
        }
        empty_list_text_view.visibility = View.VISIBLE
    }

    private fun fetchStories(forceRefresh: Boolean = false) {
        countingIdlingResource.increment()
        viewmodel.fetchStories(forceRefresh)
    }

    /* Section - UI Handlers */

    /**
     * Helper method to initialize the Stories recycler view
     */
    private fun initRecyclerView() {
        stories_recycler_view.apply {
            layoutManager = GridLayoutManager(this@StoriesActivity, calculateNumColumns())
            adapter = storyAdapter
        }
    }

    private fun initSwipeToRefresh() {
        swipe_refresh_layout.setOnRefreshListener {
            viewmodel.scrollPosition = 0
            fetchStories(true)
        }
    }

    /**
     * Helper method to initialize the click event for each
     */
    private fun initStoryItemClick() {
        val disposable = storyAdapter.getClickEvent()
            .subscribeWith(object: DisposableObserver<StoryEntity>() {
                override fun onComplete() {
                    /* no op */
                }

                override fun onNext(story: StoryEntity) {
                    Toast.makeText(this@StoriesActivity, story.title, Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    /* no op */
                }

            })
        compositeDisposable.add(disposable)
    }

    /**
     * Helper method to toggle the progress bar's visibility
     *
     * @param isBusy: Boolean flag indicating whether or not the progress bar has to be displayed
     */
    private fun toggleBusy(isBusy: Boolean) {
        if (swipe_refresh_layout.isRefreshing) {
            return
        }

        progressBar.visibility = if (isBusy) View.VISIBLE else View.GONE
    }

    /**
     * Helper method to calculate the number of columns for the recycler view's grid based on the
     * screen size and column width
     *
     * @return: Number of columns
     */
    private fun calculateNumColumns() : Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val dimension = resources.getDimensionPixelSize(R.dimen.item_card_width) / displayMetrics.density
        return (screenWidthDp / dimension).toInt()
    }
}
