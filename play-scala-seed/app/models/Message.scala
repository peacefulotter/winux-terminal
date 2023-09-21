package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Message(
  x: String
) {
  override def toString: String = Json.toJson(this).toString()
}

object Message {
  implicit val messageFormat: Format[Message] = Json.valueFormat[Message]
}
