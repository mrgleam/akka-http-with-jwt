package team.odds

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive1, Route}
import akka.http.scaladsl.server.Directives.{complete, concat, pathPrefix}
import pdi.jwt.JwtClaim
import team.odds.domains.Security.authenticated

class SecureRoutesComponent(routes: (JwtClaim => Route)*)(implicit val system: ActorSystem[_]) {
  implicit val secretKey: String = system.settings.config.getString("my-app.security.secret")
  def auth: Directive1[Either[String, JwtClaim]] = authenticated()

  val secureRoutes: Route =
    pathPrefix("secure") {
      auth {
        case Right(claims) => concat(routes.map(_(claims)):_*)
        case Left(e) =>  complete(StatusCodes.Unauthorized, e)
      }
    }
}
