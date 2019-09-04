package com.se.stories.repository

import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity
import com.se.stories.module.ApiModule
import com.se.stories.module.DbModule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

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
class StoriesRepository(private val apiModule: ApiModule, private val dbModule: DbModule)
    : IStoriesRepository {

    /**
     * Function to fetch all stories
     *
     * @param useCacheOnly: Boolean flag indicating whether to use the cache only as the data source
     *
     * @return list of StoryEntity
     *
     * @see StoryEntity
     */
    override fun getStories(useCacheOnly: Boolean): Single<List<StoryEntity>> {
        return if (useCacheOnly) {
            getStoriesFromDb()
        } else {
            getStoriesFromApi()
        }
    }

    /**
     * Function to fetch stories by ID from the database
     *
     * @param id: Story ID
     *
     * @return list of StoryEntity
     *
     * @see StoryEntity
     */
    override fun getStoryById(id: String): Single<List<StoryEntity>>
            = dbModule.getItemDatabase().storiesDao().getStoryById(id)

    /**
     * Helper method to fetch stories from the API
     *
     * @return list of StoryEntity
     */
    private fun getStoriesFromApi(): Single<List<StoryEntity>> {
        return apiModule.getStoriesApi()
            .getStories()
            .subscribeOn(Schedulers.io())
            .flatMap { stories ->
                val storyEntities = mutableListOf<StoryEntity>()
                val userEntities = mutableListOf<UserEntity>()

                for (story in stories.stories) {
                    val userEntity = UserEntity(
                        story.user.name,
                        story.user.avatar,
                        story.user.fullName
                    )
                    userEntities.add(userEntity)

                    val storyEntity = StoryEntity(
                        story.id,
                        story.title,
                        story.cover,
                        story.user.name
                    )
                    storyEntities.add(storyEntity)
                }

                dbModule.getItemDatabase().userDao().deleteAllUsers()
                dbModule.getItemDatabase().storiesDao().deleteAllStories()

                dbModule.getItemDatabase().userDao().insertAllUsers(userEntities)
                dbModule.getItemDatabase().storiesDao().insertAllStories(storyEntities)

                Single.just(storyEntities)
            }
    }

    /**
     * Helper method to fetch stories from the database
     *
     * @return list of StoryEntity
     */
    private fun getStoriesFromDb() = dbModule.getItemDatabase().storiesDao().getAllStories()
}
