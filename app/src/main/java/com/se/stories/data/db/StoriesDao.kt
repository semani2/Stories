package com.se.stories.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.se.stories.data.db.entities.StoryEntity
import io.reactivex.Single

@Dao
interface StoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStories(users: List<StoryEntity>)

    @Query("SELECT * from stories")
    fun getAllStories(): Single<List<StoryEntity>>

    @Query("SELECT * from stories where id = :id")
    fun getStoryById(id: String): Single<List<StoryEntity>>

    @Query("DELETE from users")
    fun deleteAllStories()
}
