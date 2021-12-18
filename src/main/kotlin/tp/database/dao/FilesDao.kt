package tp.database.dao

import tp.models.FileInfo
import java.util.*

interface FilesDao {

    fun getById(id: UUID): FileInfo?

    fun getAll(): List<FileInfo>

    fun save(file: FileInfo): FileInfo

}