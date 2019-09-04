package com.se.stories.viewmodel

import androidx.lifecycle.ViewModel
import com.se.stories.module.ConnectivityModule
import com.se.stories.repository.IStoriesRepository

class StoriesActivityViewModel(private val repository: IStoriesRepository, private val connectivityModule: ConnectivityModule) : ViewModel() {
}
