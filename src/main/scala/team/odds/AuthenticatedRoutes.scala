package team.odds

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import pdi.jwt.JwtClaim

class AuthenticatedRoutes()(implicit val system: ActorSystem[_]) {
  def routes(claims: JwtClaim) =
    (path("secureEndpoint") & get) {
      complete(StatusCodes.OK, s"User with information: ${claims.toString()} accessed authorized endpoint!")
    }
}