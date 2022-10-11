package team.odds

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import pdi.jwt.{Jwt, JwtClaim}
import team.odds.domains.Security.{LoginRequest, LoginResponse}

import java.time.Clock
import java.util.concurrent.TimeUnit

class LoginRoutes()(implicit val system: ActorSystem[_]) {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  implicit val clock: Clock = Clock.systemUTC

  implicit val secretKey = system.settings.config.getString("my-app.security.secret")

  // Mock username, password
  val superSecretPasswordDb = Map(
    "admin" -> "admin",
    "santi" -> "P@ssw0rd"
  )

  // Mock check username and password
  def checkPassword(username: String, password: String): Boolean =
    superSecretPasswordDb.contains(username) && superSecretPasswordDb(username) == password

  def createToken(username: String, expirePeriodInDays: Int): String = {
    val claims = JwtClaim(
      issuer = Some("example"),
      content = s"""{"user":"$username"}"""
    ).issuedNow.expiresIn(TimeUnit.DAYS.toSeconds(expirePeriodInDays))

    Jwt.encode(claims, secretKey, domains.Security.algorithm)
  }

  val loginRoute: Route =
    post {
      entity(as[LoginRequest]) {
        case LoginRequest(username, password) if checkPassword(username, password) =>
          val token = createToken(username, 1)
          complete(StatusCodes.OK, LoginResponse(token))
        case _ => complete(StatusCodes.Unauthorized)
      }
    }
}
