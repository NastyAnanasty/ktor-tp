package tp.plugins

import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tp.database.table.Files

fun Application.configureDatabase() {
    val db = Database.connect(
        "jdbc:h2:file./orm/file_logs;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        user = "root",
        password = ""
    ).apply {
        transaction(this) { SchemaUtils.create(Files) }
    }
}