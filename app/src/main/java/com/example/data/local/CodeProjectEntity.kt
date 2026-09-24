package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code_projects")
data class CodeProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val prompt: String,
    val language: String,
    val mode: String,
    val filesJson: String,
    val explanation: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
