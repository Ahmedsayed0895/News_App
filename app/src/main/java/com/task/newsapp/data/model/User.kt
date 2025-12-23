package com.task.newsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val name: String,
    val email: String,
    val password: String,

)