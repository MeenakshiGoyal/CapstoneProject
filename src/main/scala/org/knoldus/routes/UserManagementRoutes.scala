package org.knoldus.routes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.knoldus.jsonSupport.UserJsonProtocol
import org.knoldus.models.{Login, Register, User}
import org.knoldus.dao.Dao
import org.knoldus.registry.UserRegistryActor.ActionPerfomed
import org.knoldus.registry.{UserRegistryActor, actorUser}
import org.knoldus.service.UserManagementService

import scala.concurrent.Future
import scala.util.{Failure, Success}

class UserManagementRoutes extends UserJsonProtocol with SprayJsonSupport {

  val userManagementService = new UserManagementService(new actorUser)

  val userRoutes: Route =
    pathPrefix("user") {
      path("login") {
        post & entity(as[Login]) { loginRequest =>
          val result = userManagementService.login(loginRequest).map {
            case true =>

              val token = tokenAuthorization.createToken(loginRequest.username, 2)
              respondWithHeader(RawHeader("Access-Token", token))
              complete(StatusCodes.OK)

              complete(StatusCodes.Unauthorized)
          }
          complete(result)
        }

      } ~
        post {
          path("register") {
            (post & entity(as[Register])) { createUserRequest =>
              onComplete(userManagementService.register(createUserRequest)) {
                case Success(value) =>
                  complete((StatusCodes.OK, token))
                case Failure(fail)=>
                  complete(StatusCodes.Unauthorized)
              }
            }
          }
        }~
        post{
        entity(as[User]){
          users => userManagementService.createUser(users)
            complete(StatusCodes.OK)
        }
        } ~
        get{
          path("getAllUsers"){
           userManagementService.getAllUser
            complete(StatusCodes.OK)
          }~
            delete{
            path("delete"){
               user => userManagementService.deleteUser(user)
                complete(StatusCodes.OK)
                }
            } ~
        path( "uodate"/Segment/Segment){ (id , name:String) =>
          put {
           userManagementService.updateUser(id , name))
              complete(StatusCodes.OK)
            }
          }

        } ~
        path("createmoderator") =>
        post{
          userManagementService.createModerator("1")
          complete(StatusCodes.OK)
        } ~

      get {
      path("getusersbyid"/Segment) { id =>
        userManagementService.getById(id)
        complete(StatusCodes.OK)
        }
      } ~
        path("disable"/Segment) { id =>
          userManagementService.disableUser(id)
          complete(StatusCodes.OK)
        } ~
        path("enable user"/Segment){
          id => userManagementService.enableUser(id)
            complete(StatusCodes.OK)
        }
}
}






