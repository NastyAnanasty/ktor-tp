package tp.models

enum class ChangeType { CREATE, UPDATE, DELETE}


data class Notification<T>(val type: ChangeType, val entity: T)

typealias FileNotification = Notification<FileInfo?>