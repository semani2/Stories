package com.se.stories.data.api

import com.se.stories.data.api.models.Stories
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Retrofit Interface to fetch stories from the API
 */
interface ApiInterface {

    @GET("stories?offset=0&limit=10&fields=stories(id,title,cover,user)&filter=new")
    fun getStories() : Single<Stories>
}
