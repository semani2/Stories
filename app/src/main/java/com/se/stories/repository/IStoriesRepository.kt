package com.se.stories.repository

import com.se.stories.data.db.entities.StoryEntity
import io.reactivex.Single

/**
 * IStoriesRepository interface abstracts out the data source from the viewmodels
 */
interface IStoriesRepository {

    fun getStories(useCacheOnly: Boolean): Single<List<StoryEntity>>

    fun getStoryById(id: String): Single<List<StoryEntity>>

}
