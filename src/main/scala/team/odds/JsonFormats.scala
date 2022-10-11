package team.odds

import team.odds.UserRegistry.ActionPerformed
import team.odds.domains.Security.{LoginRequest, LoginResponse}

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val loginRequestJsonFormat = jsonFormat2(LoginRequest)
  implicit val loginResponseJsonFormat = jsonFormat1(LoginResponse)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
//#json-formats
