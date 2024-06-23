package com.example.mystoryapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapp.retrofit.ListStoryPagingResponse

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryPagingResponse>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryPagingResponse>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}