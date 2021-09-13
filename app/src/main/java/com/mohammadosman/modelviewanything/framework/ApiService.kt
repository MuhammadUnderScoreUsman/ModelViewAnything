package com.mohammadosman.modelviewanything.framework

import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.domain.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("posts/{postId}/comments")
    suspend fun getCommentList(@Path("postId") id: Int): List<Comment>

    @GET("comments")
    suspend fun getAllComments(): List<Comment>

}