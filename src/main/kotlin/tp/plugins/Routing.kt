package tp.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import tp.database.dao.FilesDao
import tp.database.dao.impl.FilesDaoImpl
import tp.excepitons.FileEmptyPathException
import tp.models.FileInfo
import tp.services.FileService
import tp.toStringExt
import tp.writeTo
import java.io.File
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException
import java.nio.file.NoSuchFileException
import java.sql.Timestamp
import java.util.*

fun Application.configureRouting(service: FileService) {


    routing {

        get("/ping") {
            call.respondText("ping")
        }

        post("/upload") {
            val userName = call.request.header("user-name") ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            try {
                call.receiveMultipart().apply {
                    val userFolder = File("uploads/$userName/")
                    if (!userFolder.exists()) {
                        userFolder.mkdir()
                    }
                    val firstPart = readPart() ?: throw IllegalArgumentException()

                    val createAt = Timestamp(System.currentTimeMillis())
                    var realFileName = ""
                    if (firstPart is PartData.FileItem) {
                        realFileName = firstPart.originalFileName as String
                    }
                    val resultFile = File("uploads/$userName/$realFileName")
                    val response = service.save(
                        FileInfo(
                            fileName = resultFile.nameWithoutExtension,
                            fileType = resultFile.extension,
                            author = userName,
                            createAt = createAt.toStringExt()
                        )
                    )
                    val file = File("uploads/$userName/${response.id}")
                    firstPart.writeTo(file)
                    forEachPart { part ->
                        part.writeTo(file)
                        part.dispose
                    }
                    call.respond(HttpStatusCode.OK, response)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            }
        }

        get("/download") {
            try {
                val id = call.request.queryParameters["id"]?.let(UUID::fromString) ?: throw FileEmptyPathException()
                val fileInfo = service.getById(id)?: throw FileNotFoundException()
                val file = File("uploads/${fileInfo.author}/${fileInfo.id}")
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "${fileInfo.fileName}.${fileInfo.fileType}")
                        .toString()
                )
                call.respondFile(file)
            } catch (e: FileEmptyPathException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (e: NoSuchFileException) {
                call.respond(HttpStatusCode.NotFound)
            } catch (e: FileNotFoundException) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/files") {
            call.respond(HttpStatusCode.OK, service.getAll())
        }
    }
}
