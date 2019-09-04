package com.se.stories.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.se.stories.data.db.StoriesDao
import com.se.stories.data.db.UserDao
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Tests for the UserDao
 */
@RunWith(AndroidJUnit4::class)
class UserDaoTest : BaseDaoTest() {

    /**
     * Tests that the UserDao returns an empty list for getUserByName()
     * when no data has been inserted
     */
    @Test
    fun `test_get_user_by_name_returns_empty_list_when_no_data_inserted`() {
        val users = userDao.getUserByName(testUser1.name).blockingGet()
        assertEquals(users.size, 0)
    }

    /**
     * Tests that UserDao returns requested user for getUserByName()
     */
    @Test
    fun `test_get_user_by_name_returns_requested_user`() {
        val users = mutableListOf<UserEntity>()
        users.add(testUser1)
        users.add(testUser2)

        userDao.insertAllUsers(users)

        val dbUsers = userDao.getUserByName(testUser1.name).blockingGet()
        assertEquals(dbUsers.size, 1)
        assertEquals(dbUsers[0].name, testUser1.name)
    }

    /**
     * Tests that UserDao returns an empty list for getUserByName()
     * for invalid user name
     */
    @Test
    fun `test_get_user_by_name_returns_empty_list_for_invalid_user_name`() {
        val users = mutableListOf<UserEntity>()
        users.add(testUser1)
        users.add(testUser2)

        userDao.insertAllUsers(users)

        val dbUsers = userDao.getUserByName(testUser3.name).blockingGet()
        assertEquals(dbUsers.size, 0)
    }

    /**
     * Tests that UserDao successfully deletes all records from the database
     * for deleteAllUsers()
     */
    @Test
    fun `test_delete_all_users_removes_all_cached_users`() {
        val users = mutableListOf<UserEntity>()
        users.add(testUser1)
        users.add(testUser2)
        users.add(testUser3)

        userDao.insertAllUsers(users)

        val dbUsers = userDao.getAllUsers().blockingGet()
        assertEquals(dbUsers.size, 3)

        userDao.deleteAllUsers()

        val newDbUsers = userDao.getAllUsers().blockingGet()
        assertEquals(newDbUsers.size, 0)
    }

    /**
     * Tests that deleting an user, deletes their stories
     * respecting the foreign key constraint
     */
    @Test
    fun `test_deleting_user_deletes_corresponding_stories`() {
        val users = mutableListOf<UserEntity>()
        users.add(testUser1)
        users.add(testUser2)
        users.add(testUser3)

        userDao.insertAllUsers(users)
        val dbUsers = userDao.getAllUsers().blockingGet()
        assertEquals(dbUsers.size, 3)

        insertTestStories()
        val dbStories = storiesDao.getAllStories().blockingGet()
        assertEquals(dbStories.size, 3)

        userDao.deleteAllUsers()
        val newDbUsers = userDao.getAllUsers().blockingGet()
        assertEquals(newDbUsers.size, 0)

        val deletedDbStories = storiesDao.getAllStories().blockingGet()
        assertEquals(deletedDbStories.size, 0)
    }

    /**
     * Helper method to insert test stories into database
     */
    private fun insertTestStories() {
        val stories = mutableListOf<StoryEntity>()
        stories.add(testStory1)
        stories.add(testStory2)
        stories.add(testStory3)

        storiesDao.insertAllStories(stories)
    }
}
