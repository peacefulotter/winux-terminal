package models

import play.api.libs.json.{JsNumber, JsObject, JsString}
import terminal.cmds.{ResData, Status}

abstract class Response(val status: Status.Value) {
	def toJson: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
	))
}

object Response {
	case class Success[T](data: ResData[T]) extends Response(Status.Success) {
		override def toJson: JsObject = super.toJson ++ JsObject(Seq(
			"name" -> JsString(data.name),
			"data" -> data.json
		))
	}
	case class Failure[T](msg: String) extends Response(Status.Error) {
		override def toJson: JsObject = super.toJson ++ JsObject(Seq(
			"data" -> JsString(msg)
		))
	}
	case class Nothing[T]() extends Response(Status.Nothing)
}
