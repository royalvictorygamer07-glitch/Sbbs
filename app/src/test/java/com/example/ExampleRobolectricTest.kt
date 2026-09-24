package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GenerationMode
import com.example.data.model.ProgrammingLanguage
import com.example.data.repository.AutonomousCodeEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AK EXPLOITS", appName)
  }

  @Test
  fun `test maria bakery project generation`() {
    val bakeryFiles = com.example.data.repository.MariaAgentEngine.createBakeryWebsiteProject()
    assertTrue(bakeryFiles.isNotEmpty())
    assertTrue(bakeryFiles.any { it.name == "index.html" })
    assertTrue(bakeryFiles.any { it.name == "style.css" })
    assertTrue(bakeryFiles.any { it.name == "script.js" })
  }

  @Test
  fun `test autonomous code generator produces files`() {
    val project = AutonomousCodeEngine.generateAutonomousSolution(
        prompt = "Create high performance async pipeline",
        language = ProgrammingLanguage.PYTHON,
        mode = GenerationMode.FULL_APP
    )
    assertTrue(project.files.isNotEmpty())
    assertTrue(project.files.any { it.name.endsWith(".py") })
  }
}

