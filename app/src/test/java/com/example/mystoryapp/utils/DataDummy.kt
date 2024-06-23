package com.example.mystoryapp.utils

import com.example.mystoryapp.retrofit.ListStoryPagingResponse

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryPagingResponse> {
        val storyList: MutableList<ListStoryPagingResponse> = arrayListOf()
        for (i in 0..5) {
            val story = ListStoryPagingResponse(
                "Title $i",
                "this is name",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                null,
                null,
                "This is description",
                "2022-02-22T22:22:22Z",
            )
            storyList.add(story)
        }
        return storyList
    }
}