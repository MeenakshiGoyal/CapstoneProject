package org.knoldus.dao

import akka.http.scaladsl.server.directives.Credentials
import org.knoldus.models.{Login, Register}

import scala.concurrent.Future

trait Dao[User] {

  def createUser(user: User): Future[Boolean]

  def login(credentials: Login): Future[Boolean]

  def register(credentials:Register):Future[Boolean]

  def createModerator(id: String): Future[Boolean]

  def disableUser(id: String): Future[Boolean]

  def getUserById(id: Option[String]): Future[User]

  def getAllUsers: Future[List[User]]

  def updateUser(id: User, user: User): Future[Boolean]

  def delete(id: Option[String]):Future[Boolean]

  def enableUser(id:String):Future[Boolean]

  def updateUserName(user: User, name:String):Future[Boolean]




}



