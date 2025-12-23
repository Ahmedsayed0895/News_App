package com.task.newsapp.data.repository

import com.task.newsapp.data.dataSource.local.dao.UserDao
import com.task.newsapp.data.model.User
import com.task.newsapp.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun registerUser(user: User) = userDao.registerUser(user)

    override suspend fun loginUser(email: String, password: String): User? =
        userDao.loginUser(email, password)

    override suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
}