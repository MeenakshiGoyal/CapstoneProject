package org.knoldus.Authorization
import org.knoldus.jsonSupport.UserJsonProtocol
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}

import java.util.concurrent.TimeUnit
import scala.util.{Failure, Success, Try}

object TokenAuthorization extends UserJsonProtocol {

  val algorithm = JwtAlgorithm.HS256
  val secretKey = "secretKey"

  def createToken(username:String, expirationPeriodInDays: Int): String = {
    val claims = JwtClaim(
      expiration = Some(System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(expirationPeriodInDays)),
      issuedAt = Some(System.currentTimeMillis() / 1000),
      issuer = Some("meenakshigoyal.com")
    )
    JwtSprayJson.encode(claims, secretKey, algorithm)
  }

  def tokenDecoder(token:String):Try[JwtClaim] = JwtSprayJson.decode(token,secretKey,Seq(algorithm))

  def isTokenValid(token: String): Boolean = JwtSprayJson.isValid(token, secretKey, Seq(algorithm))

  def isTokenExpired(token: String): Boolean = JwtSprayJson.decode(token, secretKey, Seq(algorithm)) match {
    case Success(claims) => claims.expiration.getOrElse(0L) < System.currentTimeMillis() / 1000
    case Failure(_) => true
  }

}
