package org.knoldus.service

import org.knoldus.dao.Dao
import org.knoldus.models.{User, UserRole}
import org.knoldus.registry.UserRepo
import org.mockito.Mockito.{mock, when}
import org.scalatest.flatspec.AsyncFlatSpec
import scala.concurrent.duration._


import scala.concurrent.{Await, Future}

class UserManagementServiceSpec extends AsyncFlatSpec {
   val  mockedService : Dao[User] = mock[UserRepo]
  val userManagementService = new UserManagementService(mockedService)

  val user = User(Some("1"),"Meenakshi","123",10,UserRole.Admin)

  it should "create any user" in {
    when(mockedService.createUser(user))thenReturn Future.successful(true)
    val result = Await.result(userManagementService.createUser(user),10 seconds)
    assert(result == true)
  }

  it should "not create any user" in {
    when(mockedService.createUser(user))thenReturn Future.successful(false)
    val result = Await.result(userManagementService.createUser(user),10 seconds)
    assert(result == false)
  }

  it should "get all users" in {
    when(mockedService.getAllUsers)thenReturn Future.successful(true)
    val result = Await.result(userManagementService.getAllUser,10 seconds)
    assert(result == true)
  }
  it should "not get all users" in {
    when(mockedService.getAllUsers)thenReturn Future.successful(false)
    val result = Await.result(userManagementService.getAllUser,10 seconds)
    assert(result == false)
  }

  it should "get users by id" in {
    when(mockedService.getUserById(user.id)) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.getById(user.id), 10 seconds)
    assert(result == true)
  }
  it should "not get users by id" in {
    when(mockedService.getUserById(user.id)) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.getById(user.id), 10 seconds)
    assert(result == false)
  }

  it should "delete a user" in {
    when(mockedService.delete(user.id)) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.deleteUser(user), 10 seconds)
    assert(result == true)
  }
  it should "not delete a user" in {
    when(mockedService.delete(user.id)) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.deleteUser(user), 10 seconds)
    assert(result == false)
  }

  it should "update a user" in {
    when(mockedService.updateUser(user,user)) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.updateUser(user,user), 10 seconds)
    assert(result == true)
  }
  it should "not update any user" in {
    when(mockedService.updateUser(user,user)) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.updateUser(user,user), 10 seconds)
    assert(result == false)
  }

  it should "update a username" in {
    when(mockedService.updateUserName(user,"Anshika")) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.updateUserName(user,"Anshika"), 10 seconds)
    assert(result == true)
  }
  it should "not update a username" in {
    when(mockedService.updateUserName(user,"Anshika")) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.updateUserName(user,"Anshika"), 10 seconds)
    assert(result == false)
  }

  it should "disable any user" in {
    when(mockedService.disableUser(user.id)) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.disableUser(user.id) , 10 seconds)
    assert(result == true)
  }
  it should " not disable any user" in {
    when(mockedService.disableUser(user.id)) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.disableUser(user.id) , 10 seconds)
    assert(result == false)
  }

  it should "enaable any user" in {
    when(mockedService.enableUser(user.id)) thenReturn Future.successful(true)
    val result = Await.result(userManagementService.enableUser(user.id) , 10 seconds)
    assert(result == true)
  }
  it should " not enable any user" in {
    when(mockedService.enableUser(user.id)) thenReturn Future.successful(false)
    val result = Await.result(userManagementService.enableUser(user.id) , 10 seconds)
    assert(result == false)
  }











}
