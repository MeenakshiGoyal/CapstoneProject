package org.knoldus.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.knoldus.models.{Login, Register, User, UserRole}
import spray.json._

  trait UserJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit object UserTypeJsonFormat extends JsonFormat[UserRole.Value] {
      def write(obj: UserRole.Value): JsValue = JsString(obj.toString)
      def read(json: JsValue): UserRole.Value = json match {
        case JsString(str) => UserRole.withName(str)
        case _             => throw DeserializationException("Enum string expected")
      }
    }
  implicit val registerFormat = jsonFormat3(Register)
  implicit val userFormat = jsonFormat5(User)
  implicit val loginFormat  = jsonFormat2(Login)
}
