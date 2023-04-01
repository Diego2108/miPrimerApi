package com.example.routes

import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val users = mutableListOf(
    User(1,"Diego", 27,"castaneda.cortes.diego@gmail.com"),
    User(2,"Gody",28,"diego.castanieda0895@gmail.com"),
)

fun Route.userRouting() {
    route("/user") {
        get {
            if (users.isNotEmpty()) {
                call.respond(users)
            } else {
                call.respondText("No hay usuarios", status = HttpStatusCode.OK)
            }
        }

        get("{id}"){
            val id = call.parameters["id"] ?: return@get call.respondText (
                "Id no encontrado",
                status = HttpStatusCode.BadRequest
            )

            val user = users.find { it.id == id.toInt() } ?: return@get call.respondText(
                "Usuario con id: $id no encontrado",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        post {
            val user = call.receive<User>()
            users.add(user)
            call.respondText("Usuario creado correctamente", status = HttpStatusCode.Created)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Id no encontrado",
                status = HttpStatusCode.BadRequest
            )
            if (users.removeIf{ it.id == id.toInt()}) {
                call.respondText("Usuario eliminado correctamente",
                    status = HttpStatusCode.Accepted)
            } else {
                call.respondText("No encontrado",
                    status = HttpStatusCode.NotFound)
            }
        }
    }
}