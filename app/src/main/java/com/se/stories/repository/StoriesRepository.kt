package com.se.stories.repository

import com.se.stories.module.ApiModule
import com.se.stories.data.model.UserStory
import com.se.stories.module.DbModule
import io.reactivex.Single

/**
 * Implementation of the IStoriesRepository
 *
 * @param apiModule: Instance of the ApiModule
 * @param dbModule: Instance of the DbModule
 *
 * @see IStoriesRepository
 * @see ApiModule
 * @see DbModule
 */
class StoriesRepository(val apiModule: ApiModule, val dbModule: DbModule) : IStoriesRepository {
    override fun getStories(): Single<List<UserStory>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStoryById(id: String): Single<UserStory> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
