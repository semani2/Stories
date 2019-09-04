package com.se.stories.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.se.stories.data.db.StoriesDao
import com.se.stories.data.db.StoriesDatabase
import com.se.stories.data.db.UserDao
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity
import org.junit.After
import org.junit.Before

open class BaseDaoTest {
    protected var testUser1 = UserEntity(
        "user_name_1",
        "avatar",
        "User1"
    )

    protected var testUser2 = UserEntity(
        "user_name_2",
        "avatar",
        "User2"
    )

    protected var testUser3 = UserEntity(
        "user_name_3",
        "avatar",
        "User3"
    )

    protected var testStory1 = StoryEntity(
        "story_1",
        "Story",
        "cover_image",
        testUser1.name
    )

    protected var testStory2 = StoryEntity(
        "story_2",
        "Story",
        "cover_image",
        testUser2.name
    )

    protected var testStory3 = StoryEntity(
        "story_3",
        "Story",
        "cover_image",
        testUser1.name
    )

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
