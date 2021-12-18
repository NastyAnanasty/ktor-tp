package tp.database.table

import org.jetbrains.exposed.dao.UUIDTable

object Files: UUIDTable() {
    val fileName = varchar("fileName", 256)
    val fileType = varchar("fileType", 20)
    val author = varchar("author", 30)
    val createAt = datetime("createAt")
}