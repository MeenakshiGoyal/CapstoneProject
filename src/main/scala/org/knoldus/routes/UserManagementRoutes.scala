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

class UserManagementRoutes(userManagementService: UserManagementService , tokenAuthorization : TokenAuthorization) extends UserJsonProtocol with SprayJsonSupport {

  val userRoutes: Route =
    pathPrefix("user") {
      path("login") {
        post & entity(as[Login]) { loginRequest =>
         onComplete(userManagementService.login(loginRequest)){
            case Success(value) =>
              val token = tokenAuthorization.createToken(loginRequest.username, 2)
              respondWithHeader(RawHeader("Access-Token", token))
              complete(StatusCodes.OK)
            case Failure(exception) =>
              complete(StatusCodes.Unauthorized)
        }
      } ~
        post {
          path("register") {
            (post & entity(as[Register])) { createUserRequest =>
              onComplete(userManagementService.register(createUserRequest)) {
                case Success(value) =>
                  val token = tokenAuthorization.createToken(loginRequest.username, 2)
                  respondWithHeader(RawHeader("Access-Token", token))
                  complete((StatusCodes.OK))
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
        path( "update"/Segment/Segment){ (id , name:String) =>
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
          get {
            userManagementService.disableUser(id)
            complete(StatusCodes.OK)
          }
        } ~
        path("enable user"/Segment) { id =>
          get {
            userManagementService.enableUser(id)
            complete(StatusCodes.OK)
          }
        }
      }

    }
}









