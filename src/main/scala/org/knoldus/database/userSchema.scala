package org.knoldus.database
import slick.ast.BaseTypedType
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.{JdbcType, MySQLProfile}
import slick.lifted.{ProvenShape, Tag}
import org.knoldus.models.{User, UserRole}
import org.knoldus.models.UserRole.{Customer, Moderator, UserRole}

import scala.concurrent.{ExecutionContext, Future}


class UserSchema (tag:Tag) extends Table[User](tag , "usertable"){

  implicit val enumMapper: JdbcType[UserRole] with BaseTypedType[UserRole] = MappedColumnType.base[UserRole ,String](
    category => category.toString,
    string => UserRole.withName(string)
  )

  def id : Rep[Option[String]] = column[Option[String]]("id")
  def password : Rep[String] = column[String]("password")
  def name: Rep[String] = column[String]("name")
  def reward   : Rep[Int] = column[Int]("reward")
  def userRole : Rep[UserRole] = column[UserRole]("userRole")
  def * : ProvenShape[User] = (id,password,name,reward,userRole) <> (User.tupled, User.unapply)
}

class UserDb(db:MySQLProfile.backend.DatabaseDef)(implicit ec:ExecutionContext)extends TableQuery(new UserSchema(_)){
  implicit val enumMapper: JdbcType[UserRole] with BaseTypedType[UserRole.Value] = MappedColumnType.base[UserRole ,String](
    category => category.toString,
    string => UserRole.withName(string)
  )

  def createTable:Future[Unit] = {
    val query = TableQuery[UserSchema].schema.createIfNotExists
    db.run(query)
  }

  def insertUser(user: User):Future[Int] = {
    val query = this += user
    db.run(query)
  }
  def getById(id:Option[String]):Future[Seq[User]] = {
    val query = this.filter(_.id === id).result
    db.run(query)
  }
  def delete(user: User):Future[Int]={
    val query = this.filter(_.id === user.id).delete
    db.run(query)
  }
  def getAllUsers:Future[Seq[User]] ={
    db.run(this.result)
  }
  def updateName(id:Option[String], name:String):Future[Int]={
    val query = this.filter(_.id === id).map(user =>
      (user.name)).update(name)
    db.run(query)
  }
  def update(id:Option[String], name:String,password :String,rewards:Int,role:UserRole):Future[Int]={
    val query = this.filter(_.id === id).map(user =>
      (user.name,user.password,user.reward,user.userRole)).update(name,password,rewards,role)
    db.run(query)
  }

  def createModerator(id : Option[String]) : Future[Int] ={
    db.run(this.filter(_.id === id).map(_.userRole).update(Moderator))
  }
  def disableUser(id : Option[String]): Future[Int] = {
    db.run(this.filter(_.id === id).map(_.userRole).update(Some(0)))
  }

  def enableUser(id : Option[String]): Future[Int] = {
    db.run(this.filter(_.id === id).map(_.userRole).update(Some(1)))
  }

}







