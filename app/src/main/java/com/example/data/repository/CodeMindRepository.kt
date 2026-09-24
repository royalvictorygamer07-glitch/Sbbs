package com.example.data.repository

import com.example.data.local.CodeProjectDao
import com.example.data.local.CodeProjectEntity
import com.example.data.model.CodeFile
import com.example.data.model.GenerationMode
import com.example.data.model.GenerationSettings
import com.example.data.model.ProgrammingLanguage
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class CodeMindRepository(
    private val dao: CodeProjectDao,
    private val geminiService: GeminiService = GeminiService()
) {
    val allProjects: Flow<List<CodeProjectEntity>> = dao.getAllProjects()
    val favoriteProjects: Flow<List<CodeProjectEntity>> = dao.getFavoriteProjects()

    suspend fun getProjectById(id: Long): CodeProjectEntity? = dao.getProjectById(id)

    fun searchProjects(query: String): Flow<List<CodeProjectEntity>> = dao.searchProjects(query)

    suspend fun deleteProject(id: Long) = dao.deleteById(id)

    suspend fun toggleFavorite(id: Long, current: Boolean) = dao.setFavorite(id, !current)

    suspend fun generateCode(
        prompt: String,
        language: ProgrammingLanguage,
        mode: GenerationMode,
        settings: GenerationSettings
    ): AutonomousCodeEngine.ParsedProject {
        // First try calling Gemini with the configured model & key
        val result = geminiService.generateCode(
            prompt = prompt,
            targetLanguage = language.displayName,
            mode = mode.title,
            modelName = settings.modelName,
            temperature = settings.temperature,
            customApiKey = settings.customApiKey,
            persona = settings.persona,
            unrestricted = settings.unrestrictedMode
        )

        val parsed = if (result.isSuccess) {
            val text = result.getOrNull().orEmpty()
            AutonomousCodeEngine.parseOutput(text, language, prompt)
        } else {
            // Unrestricted fallback engine: generate immediately without failing
            AutonomousCodeEngine.generateAutonomousSolution(prompt, language, mode)
        }

        // Save project into local Room database
        val title = prompt.trim().lines().firstOrNull()?.take(40) ?: "Project in ${language.displayName}"
        val filesJson = serializeFiles(parsed.files)
        val entity = CodeProjectEntity(
            title = title,
            prompt = prompt,
            language = language.id,
            mode = mode.id,
            filesJson = filesJson,
            explanation = parsed.explanation
        )
        dao.insertProject(entity)

        return parsed
    }

    suspend fun saveCustomProject(
        title: String,
        prompt: String,
        language: String,
        mode: String,
        files: List<CodeFile>,
        explanation: String
    ): Long {
        val entity = CodeProjectEntity(
            title = title,
            prompt = prompt,
            language = language,
            mode = mode,
            filesJson = serializeFiles(files),
            explanation = explanation
        )
        return dao.insertProject(entity)
    }

    fun serializeFiles(files: List<CodeFile>): String {
        val jsonArray = JSONArray()
        for (f in files) {
            val obj = JSONObject().apply {
                put("name", f.name)
                put("language", f.language)
                put("content", f.content)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    fun deserializeFiles(json: String): List<CodeFile> {
        val list = mutableListOf<CodeFile>()
        try {
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    CodeFile(
                        name = obj.getString("name"),
                        language = obj.getString("language"),
                        content = obj.getString("content")
                    )
                )
            }
        } catch (e: Exception) {
            list.add(CodeFile("Main.txt", "text", json))
        }
        return list
    }
}
