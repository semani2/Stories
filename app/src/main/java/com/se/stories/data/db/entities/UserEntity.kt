package com.se.stories.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(@PrimaryKey val name: String,
                      var avatar: String,
                      var full_name: String)
