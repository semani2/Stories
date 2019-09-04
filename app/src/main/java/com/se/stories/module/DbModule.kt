package com.se.stories.module

import android.app.Application
import androidx.room.Room
import com.se.stories.data.db.StoriesDatabase

/**
 * This class is responsible for building the Room database
 */
class DbModule(private val application: Application) {

    /**
     * Returns the StoriesDatabase instance
     *
     * @return instance of StoriesDatabase
     * @see StoriesDatabase
     */
    fun getItemDatabase(): StoriesDatabase =
        Room.databaseBuilder(application, StoriesDatabase::class.java, "stories.db")
            .fallbackToDestructiveMigration()
            .build()
}
