package team.odds.domains

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.optionalHeaderValueByName
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.{Failure, Success, Try}

object Security {
  case class LoginRequest(username: String, password: String)
  case class LoginResponse(token: String)

  val algorithm = JwtAlgorithm.HS256

  def decodeToken(token: String)(implicit secretKey: String): Try[JwtClaim] = {
    Jwt.decode(token, secretKey, Seq(algorithm))
  }

  def authenticated()(implicit secretKey: String): Directive1[Either[String, JwtClaim]] = optionalHeaderValueByName("Authorization") map { // optionalHeaderValue: Optional[String] =>
    case Some(s"Bearer $token")  => decodeToken(token) match {
      case Success(claims) => Right(claims)
      case Failure(exception) => Left(exception.getMessage)
    }
    case _ => Left("Token not found")
  }
}
