package tp

import io.ktor.http.content.*
import java.io.File

fun PartData.writeTo(file: File) {
    if( this is PartData.FileItem) {
        streamProvider().use { input ->
            file.outputStream().buffered().use { output -> input.copyTo(output) }
        }
    }
}