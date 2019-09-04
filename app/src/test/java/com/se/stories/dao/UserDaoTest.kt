package com.se.stories.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.se.stories.data.db.StoriesDatabase
import com.se.stories.data.db.UserDao
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
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: StoriesDatabase

    private var testUser1 = UserEntity(
        "user_name_1",
        "avatar",
        "User1"
    )

    private var testUser2 = UserEntity(
        "user_name_2",
        "avatar",
        "User2"
    )

    private var testUser3 = UserEntity(
        "user_name_3",
        "avatar",
        "User3"
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context,
            StoriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

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
}
