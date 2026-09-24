package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeProjectDao {
    @Query("SELECT * FROM code_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<CodeProjectEntity>>

    @Query("SELECT * FROM code_projects WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteProjects(): Flow<List<CodeProjectEntity>>

    @Query("SELECT * FROM code_projects WHERE id = :id")
    suspend fun getProjectById(id: Long): CodeProjectEntity?

    @Query("SELECT * FROM code_projects WHERE title LIKE '%' || :query || '%' OR prompt LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchProjects(query: String): Flow<List<CodeProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: CodeProjectEntity): Long

    @Update
    suspend fun updateProject(project: CodeProjectEntity)

    @Delete
    suspend fun deleteProject(project: CodeProjectEntity)

    @Query("DELETE FROM code_projects WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE code_projects SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)
}
