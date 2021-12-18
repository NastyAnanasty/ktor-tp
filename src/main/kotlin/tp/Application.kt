package tp

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tp.plugins.configureDatabase
import tp.plugins.configureRouting
import tp.plugins.configureSerialization
import tp.plugins.configureSockets
import tp.services.FileService

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        val fileService = FileService()
        configureRouting(fileService)
        configureSerialization()
        configureSockets(fileService)
        configureDatabase()

    }.start(wait = true)
}
