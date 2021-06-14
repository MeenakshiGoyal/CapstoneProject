package org.knoldus
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.knoldus.routes.UserManagementRoutes

object HttpServerApp extends App{
  implicit val system: ActorSystem = ActorSystem("User Management")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val routes = new UserManagementRoutes

  Http().bindAndHandle(routes.userRoutes, "localhost", 8080)

}
