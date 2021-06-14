package org.knoldus.registry
import org.knoldus.database.{UserDb, dbConnection}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.knoldus.dao.Dao
import org.knoldus.jsonSupport.UserJsonProtocol
import org.knoldus.models.UserRole.UserRole
import org.knoldus.models.{Login, User, UserRole}
import org.knoldus.registry.UserRegistryActor.{CreateModerator, CreateUser, DeleteUser, GetAllUsers}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object UserRegistryActor {
  final case object CreateTable
  final case class ActionPerfomed(action:String)
  final case class DisableUser(id : Option[String])
  final case class EnableUser(id: Option[String])
  final case class CreateModerator(id : Option[String])
  final case class LoginRequest(credentials: Login)
  final case class CreateUser(user:User)
  final case object GetAllUsers
  final case class UpdateUser(id : Option[String], name:String,password: String,rewards:Int,role:UserRole)
  final case class UpdateUserName(id:Option[String], name: String)
  final case class DeleteUser(id:Option[String])
  final case class GetUserByID(id: Option[String])

}

class UserRegistryActor extends UserJsonProtocol with Actor with ActorLogging {
  import UserRegistryActor._
  import context.dispatcher

  val dbConnection = new dbConnection
  val userDb = new UserDb(dbConnection.db)

  def receive:Receive = {
    case CreateUser(user) =>
    userDb.insertUser(user)
      sender()!ActionPerfomed(s"users created $user")

     case CreateTable =>
    userDb.createTable.onComplete {
      case Success(table) => sender ! User
      case Failure(failed) => println("Table not created")
    }

    case GetAllUsers =>
    userDb.getAllUsers.onComplete{
      case Success(usr) => sender ! usr
      case Failure(failed) => println("Data not found")
    }

    case GetUserByID(id) =>
      val user = userDb.getById(id)
      val userSender = sender
      user.onComplete {
        case Success(usr) => userSender ! usr
        case Failure(failureUsr) => println(s"$id user not found")
      }
    case DeleteUser(user) =>
      val user = userDb.delete(user)
      val delSender = sender
      user.onComplete {
        case Success(del) => delSender!ActionPerfomed(s"Successfully deleted $user")
        case Failure(delUser) => println(s"Unable to Delete user $user")
      }
    case UpdateUserName(id,name) =>
      val user = userDb.updateName(id ,name)
      val updateSender = sender
      user.onComplete{
        case Success(update) => updateSender ! update
        case Failure(updateUser) => println(s"$id user not found")
      }
    case UpdateUser(id,name,password,rewards,role) =>
      val user = userDb.update(id,name,password,rewards,role)
      user.onComplete{
        case Success(update) => sender ! update
        case Failure(failed) => println(s"not updated $user")
      }
    case CreateModerator(id)=>
       userDb.createModerator(id)
      sender()!ActionPerfomed("Moderator created")

    case DisableUser(id)=>
      userDb.disableUser(id)
      sender()!ActionPerfomed("Disabld user")

    case EnableUser(id)=>
      userDb.enableUser(id)
      sender()!ActionPerfomed("Enable user")
  }
}

class actorUser extends Dao[User]
{
  val system: ActorSystem = ActorSystem("System")
  val actor: ActorRef = system.actorOf(Props[UserRegistryActor])
  implicit val timeout: Timeout = Timeout(5 seconds)

  override def createUser(user: User): Future[Boolean] =
    (actor ? CreateUser(user)).mapTo[Boolean]

  override def getAllUsers(): Future[List[User]] =
    (actor ? GetAllUsers).mapTo[List[User]]

  override def updateUser(user : User, newUser : User): Future[Boolean] =
    (actor ? updateUser(user,newUser)).mapTo[Boolean]

  override def updateUserName(user: User, newName: String): Future[Boolean] =
    (actor ? updateUserName(user,newName)).mapTo[Boolean]

  override def delete(id:Option[String]): Future[Boolean] =
    (actor ? DeleteUser(id)).mapTo[Boolean]

  override def getUserById(id: Option[String]): Future[User] =
    (actor ? getUserById(id)).mapTo[User]

  override def createModerator(userId: Option[String]): Future[Boolean] =
    (actor ? CreateModerator(userId)).mapTo[Boolean]

}


