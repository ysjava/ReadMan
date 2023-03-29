package com.learndm.nnsman.novelreader.data.network.api


import com.learndm.nnsman.novelreader.data.model.Subscription
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChapterService {
    @POST("chapter/subscribe")
    fun subscribeChapter(@Query("chapterId") chapterId:String):Call<Subscription>

}