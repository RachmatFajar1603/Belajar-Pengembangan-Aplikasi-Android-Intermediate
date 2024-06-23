package com.example.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.database.StoryDatabase
import com.example.mystoryapp.retrofit.ApiService
import com.example.mystoryapp.retrofit.ListStoryPagingResponse

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(token: String): LiveData<PagingData<ListStoryPagingResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}