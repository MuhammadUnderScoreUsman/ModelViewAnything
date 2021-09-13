package com.mohammadosman.modelviewanything.usecase

import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.framework.ApiService

class GetCommentList(
    private val apiService: ApiService
) {

    suspend fun getCommentList(): List<Comment> {
        return apiService.getAllComments()
    }

    suspend fun getCommentListViaId(userId: Int): List<Comment>{
        return apiService.getCommentList(userId)
    }
}