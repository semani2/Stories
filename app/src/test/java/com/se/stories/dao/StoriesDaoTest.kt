package com.se.stories.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.se.stories.TestUtil.Companion.testStory1
import com.se.stories.TestUtil.Companion.testStory2
import com.se.stories.TestUtil.Companion.testStory3
import com.se.stories.TestUtil.Companion.testUser1
import com.se.stories.TestUtil.Companion.testUser2
import com.se.stories.TestUtil.Companion.testUser3
import com.se.stories.data.db.StoriesDao
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for StoriesDao
 *
 * @see StoriesDao
 */
@RunWith(AndroidJUnit4::class)
class StoriesDaoTest : BaseDaoTest() {

    /**
     * Tests that the Stories DAO returns an empty list for getAllStories()
     * when no data has been inserted
     */
    @Test
    fun `test_get_stories_returns_empty_list_when_no_data_inserted`() {
        val stories = storiesDao.getAllStories().blockingGet()
        assertEquals(stories.size, 0)
    }

    /**
     * Tests that the Stories DAO returns an empty list for getStoryById()
     * when no data has been inserted
     */
    @Test
    fun `test_get_stories_by_id_returns_empty_list_when_no_data_inserted`() {
        val stories = storiesDao.getStoryById(testStory1.id).blockingGet()
        assertEquals(stories.size, 0)
    }

    /**
     * Tests that the Stories DAO returns the valid inserted data for getAllStories()
     */
    @Test
    fun `test_get_all_stories_returns_inserted_data`() {
        // Insert users into DB to maintain the foreign key constraint
        insertTestUsers()

        val stories = mutableListOf<StoryEntity>()
        stories.add(testStory1)
        stories.add(testStory2)

        storiesDao.insertAllStories(stories)

        val dbStories = storiesDao.getAllStories().blockingGet()
        assertEquals(dbStories.size, stories.size)

        for (i in 0 until dbStories.size) {
            assertEquals(dbStories[i].id, stories[i].id)
        }
    }

    /**
     * Tests the StoriesDAO returns the requested story for
     * getStoryById()
     */
    @Test
    fun `test_get_story_by_id_returns_requested_story`() {
        // Insert users into DB to maintain the foreign key constraint
        insertTestUsers()

        val stories = mutableListOf<StoryEntity>()
        stories.add(testStory1)
        stories.add(testStory2)

        storiesDao.insertAllStories(stories)

        val dbStories = storiesDao.getStoryById(testStory2.id).blockingGet()
        assertEquals(dbStories.size, 1)
        assertEquals(dbStories[0].id, testStory2.id)
    }

    @Test
    fun `test_get_story_by_id_returns_empty_for_invalid_id`() {
        // Insert users into DB to maintain the foreign key constraint
        insertTestUsers()

        val stories = mutableListOf<StoryEntity>()
        stories.add(testStory1)
        stories.add(testStory2)

        storiesDao.insertAllStories(stories)

        val dbStories = storiesDao.getStoryById(testStory3.id).blockingGet()
        assertEquals(dbStories.size, 0)
    }

    @Test
    fun `test_delete_all_stories_removes_all_cached_stories`() {
        // Insert users into DB to maintain the foreign key constraint
        insertTestUsers()

        val stories = mutableListOf<StoryEntity>()
        stories.add(testStory1)
        stories.add(testStory2)
        stories.add(testStory3)

        storiesDao.insertAllStories(stories)

        val dbStories = storiesDao.getAllStories().blockingGet()
        assertEquals(dbStories.size, 3)

        storiesDao.deleteAllStories()

        val deletedDbStories = storiesDao.getAllStories().blockingGet()
        assertEquals(deletedDbStories.size, 0)
    }

    /**
     * Helper method to insert test users into database
     */
    private fun insertTestUsers() {
        val users = mutableListOf<UserEntity>()
        users.add(testUser1)
        users.add(testUser2)
        users.add(testUser3)

        userDao.insertAllUsers(users)
    }
}
