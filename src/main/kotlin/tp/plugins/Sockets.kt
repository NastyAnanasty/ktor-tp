package tp.plugins

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tp.services.FileService
import java.time.Duration

fun Application.configureSockets(service: FileService) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/getAll") {
            outgoing.send(Frame.Text(""))
            try {
                service.addChangeListener(this.hashCode()) {
                    val output = withContext(Dispatchers.IO) {
                        Gson().toJson(service.getAll())
                    }
                    outgoing.send(Frame.Text(output))
                }
                while (true) {
                    val output = withContext(Dispatchers.IO) {
                        Gson().toJson(service.getAll())
                    }
                    outgoing.send(Frame.Text(output))
                    incoming.receive()
                }
            } finally {
                service.removeChangeListener(this.hashCode())
            }
        }
    }
}
