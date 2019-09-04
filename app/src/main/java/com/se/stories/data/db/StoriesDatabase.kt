package com.se.stories.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity

@Database(entities = [UserEntity::class, StoryEntity::class], version = 1)
abstract class StoriesDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun storiesDao(): StoriesDao
}
