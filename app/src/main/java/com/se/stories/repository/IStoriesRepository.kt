package com.se.stories.repository

import com.se.stories.data.model.UserStory
import io.reactivex.Single

/**
 * IStoriesRepository interface abstracts out the data source from the viewmodels
 */
interface IStoriesRepository {

    fun getStories(): Single<List<UserStory>>

    fun getStoryById(id: String): Single<UserStory>

}
