package models

import play.api.libs.json.{JsNull, JsNumber, JsObject, JsString, JsValue}
import terminal.cmds.{ResData, Status}

abstract class Response(val status: Status.Value, val name: String, val data: JsValue) {
	def toJson: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
		"name" -> JsString(name),
		"data" -> data,
	))
}

object Response {
	case class Success[T](resData: ResData[T]) extends Response(Status.Success, resData.name, resData.json)
	case class Failure[T](msg: String) extends Response(Status.Failure, "line", JsString(msg))
	case class Nothing[T]() extends Response(Status.Nothing, "nothing", JsNull)
}
