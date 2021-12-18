package tp.database.entity

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import tp.database.table.Files
import tp.models.FileInfo
import java.util.*

class FilesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<FilesEntity>(Files)

    var fileName by Files.fileName
    var fileType by Files.fileType
    var author by Files.author
    var createAt by Files.createAt

    fun toFile()  = FileInfo(id.value, this)
}
