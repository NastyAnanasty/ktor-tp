package tp.services

import tp.database.dao.FilesDao
import tp.database.dao.impl.FilesDaoImpl
import tp.models.ChangeType
import tp.models.FileInfo
import tp.models.FileNotification
import tp.models.Notification
import java.util.*

class FileService {

    val filesDao: FilesDao = FilesDaoImpl()

    private val listeners = mutableMapOf<Int, suspend (FileNotification) -> Unit>()

    fun addChangeListener(id: Int, listener: suspend (FileNotification) -> Unit) {
        listeners[id] = listener
    }

    fun removeChangeListener(id: Int) = listeners.remove(id)

    private suspend fun onChange(type: ChangeType, entity: FileInfo? = null) {
        listeners.values.forEach {
            it.invoke(Notification(type, entity))
        }
    }

    fun getAll() = mapOf("files" to filesDao.getAll())

    fun getById(id: UUID) = filesDao.getById(id)

    suspend fun save(file: FileInfo): FileInfo {
        val result = filesDao.save(file)
        onChange(ChangeType.CREATE, result)
        return result
    }
}