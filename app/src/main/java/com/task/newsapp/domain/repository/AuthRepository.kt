package com.task.newsapp.domain.repository

import com.task.newsapp.data.model.User

interface AuthRepository {
    suspend fun registerUser(user: User)
    suspend fun loginUser(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
}