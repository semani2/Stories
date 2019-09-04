package com.se.stories.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.se.stories.RxImmediateSchedulerRule
import com.se.stories.TestUtil
import com.se.stories.data.db.entities.StoryEntity
import com.se.stories.module.ConnectivityModule
import com.se.stories.repository.IStoriesRepository
import io.reactivex.Single
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class StoriesActivityViewModelTest {

    /* Test Rules */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    /* Test Mocks */
    @Mock
    lateinit var connectivityModule: ConnectivityModule

    @Mock
    lateinit var repository: IStoriesRepository

    lateinit var viewmodel: StoriesActivityViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewmodel = StoriesActivityViewModel(repository, connectivityModule)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the
     * repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Success
     * 2. LiveData data size = 3
     * 3. LiveData exception = null
     */
    @Test
    fun `test_fetch_stories_successful`() {
        val testStories = mutableListOf<StoryEntity>()
        testStories.add(TestUtil.testStory1)
        testStories.add(TestUtil.testStory2)
        testStories.add(TestUtil.testStory3)

        Mockito.`when`(this.repository.getStories(ArgumentMatchers.anyBoolean()))
            .thenReturn(Single.just(testStories))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<StoryEntity>, Exception>>
        this.viewmodel.storiesLiveData.observeForever(observer)

        this.viewmodel.fetchStories(true)

        assertNotNull(this.viewmodel.storiesLiveData)
        assertNotNull(this.viewmodel.storiesLiveData.value)
        assertNull(this.viewmodel.storiesLiveData.value?.exception)
        assertNotNull(this.viewmodel.storiesLiveData.value?.data)
        assertEquals(this.viewmodel.storiesLiveData.value?.data?.size, testStories.size)
        assertEquals(ResourceStatus.SUCCESS, this.viewmodel.storiesLiveData.value?.status)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the
     * repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Error
     * 2. LiveData data = null
     * 3. LiveData exception = Not null
     */
    @Test
    fun `test_fetch_stories_failed_with_exception`() {
        val exceptionMessage = "Error fetching stories"
        Mockito.`when`(this.repository.getStories(ArgumentMatchers.anyBoolean()))
            .thenReturn(Single.error(Exception(exceptionMessage)))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<StoryEntity>, Exception>>
        this.viewmodel.storiesLiveData.observeForever(observer)

        this.viewmodel.fetchStories(true)

        assertNotNull(this.viewmodel.storiesLiveData)
        assertNotNull(this.viewmodel.storiesLiveData.value)
        assertNotNull(this.viewmodel.storiesLiveData.value?.exception)
        assertNull(this.viewmodel.storiesLiveData.value?.data)
        assertEquals(this.viewmodel.storiesLiveData.value?.exception?.message, exceptionMessage)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.storiesLiveData.value?.status)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the
     * repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Error
     * 2. LiveData data = null
     * 3. LiveData exception = Not null
     */
    @Test
    fun `test_fetch_stories_failed_when_repository_returns_empty_list`() {
        Mockito.`when`(this.repository.getStories(ArgumentMatchers.anyBoolean()))
            .thenReturn(Single.just(mutableListOf()))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<StoryEntity>, Exception>>
        this.viewmodel.storiesLiveData.observeForever(observer)

        this.viewmodel.fetchStories(true)

        assertNotNull(this.viewmodel.storiesLiveData)
        assertNotNull(this.viewmodel.storiesLiveData.value)
        assertNotNull(this.viewmodel.storiesLiveData.value?.exception)
        assertNull(this.viewmodel.storiesLiveData.value?.data)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.storiesLiveData.value?.status)
    }
}
