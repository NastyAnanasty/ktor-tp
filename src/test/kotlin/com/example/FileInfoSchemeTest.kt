package com.example

import com.google.gson.Gson
import net.pwall.json.schema.JSONSchema
import org.junit.Test
import tp.models.FileInfo
import java.io.FileInputStream
import org.junit.Assert.assertEquals

class FileInfoSchemeTest {

    companion object {

        const val schemaPath = "src\\test\\resources\\file_info_scheme.json"

        val testFileInfo = FileInfo(
            id = "e363f0d9-0f92-419f-8303-727fdffce25e",
            fileName = "0110annotation",
            fileType = "docx",
            author = "firstUser",
            createAt = "12.18.2021 16:45",
        )
    }

    @Test
    fun `Schema validate`() {
        val schema = JSONSchema.parse(FileInputStream(schemaPath).use { it.reader().readText() })
        val fileInfoJson = Gson().toJson(testFileInfo)
        val output = schema.validateBasic(fileInfoJson)
        output.errors?.forEach { basicErrorEntry ->
            println("${basicErrorEntry.error} - ${basicErrorEntry.instanceLocation}")
        }
        assertEquals(true, output.errors.isNullOrEmpty())
    }
}