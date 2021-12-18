package tp.models

import tp.database.entity.FilesEntity
import java.util.*

data class FileInfo constructor(
    val id: String = EMPTY_ID,
    val fileName: String,
    val fileType: String,
    val author: String,
    val createAt: String,
) {

    init {
        require(fileName.isNotBlank()) { "fileName is blank" }
        require(fileType.isNotBlank()) { "fileType is blank " }
        require(author.isNotBlank()) { "author is blank" }
        require(createAt.isNotBlank()) { "createAt is blank " }
    }

    constructor(id: UUID, file: FilesEntity) : this(
        id.toString(),
        file.fileName,
        file.fileType,
        file.author,
        file.createAt.toString("MM.dd.yyyy HH:mm")
    )

    companion object {
        const val EMPTY_ID = ""
    }
}
