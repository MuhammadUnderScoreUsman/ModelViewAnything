package com.mohammadosman.modelviewanything.usecase

import com.mohammadosman.modelviewanything.domain.User
import com.mohammadosman.modelviewanything.framework.ApiService

class GetUser(
    private val apiService: ApiService
) {

    suspend fun getUser(userId: Int): User {
        return apiService.getUser(userId)
    }

}