package com.se.stories.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "stories", foreignKeys = [ForeignKey(entity = UserEntity::class,
    parentColumns = arrayOf("name"),
    childColumns = arrayOf("author_name"),
    onDelete = ForeignKey.CASCADE)]
)
data class StoryEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "cover") var cover: String,
    @ColumnInfo(name = "author_name") var authorName: String
)
