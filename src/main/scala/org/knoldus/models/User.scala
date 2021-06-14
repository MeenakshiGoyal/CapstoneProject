package org.knoldus.models

import UserRole.UserRole

case class User(id:Option[String] ,name:String , password : String,  rewardsPoint:Int , role: UserRole)

