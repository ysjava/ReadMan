package com.learndm.nnsman.novelreader.data.network.api

import com.learndm.nnsman.novelreader.data.model.Comment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentService {

    @POST("comment/finds")
    fun findComments(
        @Query("chapterId") chapterId: String,
        @Query("startIndex") startIndex: Int,
        @Query("endIndex") endIndex: Int
    ): Call<List<Comment>>

}