package com.se.stories.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.se.stories.data.db.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(users: List<UserEntity>)

    @Query("Select * from users where name = :name")
    fun getUserByName(name: String)
}
