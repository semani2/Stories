package com.se.stories.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.se.stories.R
import com.se.stories.adapter.StoryAdapter
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.module.ConnectivityModule
import com.se.stories.viewmodel.StoriesActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_stories.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class StoriesActivity : AppCompatActivity() {

    val viewmodel: StoriesActivityViewModel by viewModel()

    private val storiesList = mutableListOf<StoryEntity>()
    private val storyAdapter = StoryAdapter(storiesList)

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val connectivityModule: ConnectivityModule by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        initRecyclerView()
        initStoryItemClick()
        initLiveDataObservers()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    /* Section - Data */

    /**
     * Method to initialize the ViewModels' live data observers
     */
    private fun initLiveDataObservers() {

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

    /**
     * Helper method to initialize the click event for each
     */
    private fun initStoryItemClick() {
        val disposable = storyAdapter.getClickEvent()
            .subscribeWith(object: DisposableObserver<StoryEntity>() {
                override fun onComplete() {
                    /* no op */
                }

                override fun onNext(t: StoryEntity) {
                    // TODO :: Story Detail page
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
