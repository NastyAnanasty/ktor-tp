package tp.database.dao.impl

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import tp.database.dao.FilesDao
import tp.database.entity.FilesEntity
import tp.database.table.Files
import tp.models.FileInfo
import java.util.*

class FilesDaoImpl : FilesDao {
    override fun getById(id: UUID): FileInfo? = transaction {
        FilesEntity.findById(id)?.let(FilesEntity::toFile)
    }

    override fun getAll(): List<FileInfo> = transaction {
        FilesEntity.all().orderBy(Files.createAt to SortOrder.DESC).map(FilesEntity::toFile)
    }

    override fun save(file: FileInfo): FileInfo = transaction {
        FilesEntity.new {
            fileName = file.fileName
            fileType = file.fileType
            author = file.author
            createAt = DateTime.parse(file.createAt)
        }.toFile()
    }
}