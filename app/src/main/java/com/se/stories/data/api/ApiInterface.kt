package com.se.stories.data.api

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Retrofit Interface to fetch stories from the API
 */
interface ApiInterface {

    @GET
    fun getStories() : Single<Stories>
}
