package com.se.stories

import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.data.db.entities.UserEntity

class TestUtil {

    companion object {
        var testUser1 = UserEntity(
            "user_name_1",
            "avatar",
            "User1"
        )

        var testUser2 = UserEntity(
            "user_name_2",
            "avatar",
            "User2"
        )

        var testUser3 = UserEntity(
            "user_name_3",
            "avatar",
            "User3"
        )

        var testStory1 = StoryEntity(
            "story_1",
            "Story",
            "cover_image",
            testUser1.name
        )

        var testStory2 = StoryEntity(
            "story_2",
            "Story",
            "cover_image",
            testUser2.name
        )

        var testStory3 = StoryEntity(
            "story_3",
            "Story",
            "cover_image",
            testUser1.name
        )
    }
}
