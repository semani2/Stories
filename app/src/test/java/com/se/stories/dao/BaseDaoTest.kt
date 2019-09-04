package com.se.stories.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.se.stories.data.db.StoriesDao
import com.se.stories.data.db.StoriesDatabase
import com.se.stories.data.db.UserDao
import org.junit.After
import org.junit.Before

open class BaseDaoTest {
    private lateinit var db: StoriesDatabase

    protected lateinit var userDao: UserDao
    protected lateinit var storiesDao: StoriesDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context,
            StoriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        storiesDao = db.storiesDao()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

}
