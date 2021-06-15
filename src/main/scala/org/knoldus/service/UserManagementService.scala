package org.knoldus.service

import org.knoldus.dao.Dao
import org.knoldus.models.{Login, Register, User}

import scala.concurrent.Future

class UserManagementService(repository:Dao[User]) {

  def createUser(user: User): Future[Boolean] = {
    if (user.id != null)
      repository.createUser(user)
  }

  def login(login: Login): Future[Boolean] = {
    repository.login(login)
  }

  def register(register: Register):Future[Boolean]={
    repository.register(register)
  }

  def createModerator(userID: String): Future[Boolean] = {
    repository.createModerator(userID)
  }

  def disableUser(userID: String): Future[Boolean] = {
    repository.disableUser(userID)
  }

  def enableUser(userID: String): Future[Boolean] = {
    repository.enableUser(userID)
  }

  def getAllUser: Future[List[User]] = {
    repository.getAllUsers
  }

  def updateUser(user: User, newUser: User): Future[Boolean] = {
    if (user.id != newUser) {
      repository.updateUser(user, newUser)
    }
  }

  def updateUserName(user: User, newName: String): Future[Boolean] = {
    if (user.name != newName && newName.nonEmpty) {
      repository.updateUserName(user, newName)
    }

  }

  def deleteUser(user: User): Future[Boolean] = {
    repository.delete(user.id)
  }

  def getById(id: Option[String]): Future[User] = {
    repository.getUserById(id)
  }
}

