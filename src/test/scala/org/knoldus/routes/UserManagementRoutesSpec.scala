package org.knoldus.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import org.knoldus.jsonSupport.UserJsonProtocol
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.knoldus.models.{User, UserRole}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class UserManagementRoutesSpec  extends AnyWordSpec with Matchers with ScalatestRouteTest with UserJsonProtocol{
   val user = User(Some("1"),"Meenakshi","1223",1,UserRole.Admin)

 "A user" should {
   "return all the users" in {
     Get("user/getAllUsers") ~> userRoutes ~> check {
       status shouldBe StatusCodes.OK
       responseAs[List[User]] shouldBe List(user)
     }
   }

   "return a user by hitting the query parameter endpoint" in {
     Get("user/getuserbyid?id=1") ~> userRoutes ~> check {
       status shouldBe StatusCodes.OK
       responseAs[Option[User]] shouldBe Some(User(Some("1"), "Meenakshi","1223",1,UserRole.Admin))
     }
   }
   "insert a user into the database " in {
     val newUser = User(Some("2"), "Ayush","1233",1,UserRole.Customer)
     Post("user/createuser" , newUser) ~> userRoutes ~> check{
       status shouldBe StatusCodes.OK
       assert(user.contains(newUser))
     }
   }
   "Delete a user" in {
     Delete("user/delete") ~> userRoutes ~> check{
       status shouldBe StatusCodes.OK
     }
   }
   "Update a user " in {
     Put("user/update/1/Sakshi") ~> userRoutes ~> check{
       status shouldBe StatusCodes.OK
       responseAs[Option[User]] shouldBe Some(Some("1"), "Sakshi","1223",1,UserRole.Admin)
     }
   }
   "Disable a user" in {
     Get("user/disable/1") ~> userRoutes ~> check{
       status shouldBe StatusCodes.OK
     }
   }
   "  Enable a user" in {
     Get("user/enable/1") ~> userRoutes ~> check{
       status shouldBe StatusCodes.OK
     }
   }
   "Create MOderator " in {
     val newUser = User(Some("2"), "Ayush","1233",1,UserRole.Moderator)
     Post("user/createmoderator") ~> userRoutes ~> check {
       status shouldBe StatusCodes.OK
       assert(user.contains(newUser))
     }
   }
 }
}
