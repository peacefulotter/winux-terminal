package models

import play.api.libs.json.{JsNull, JsNumber, JsObject, JsString, JsValue}
import terminal.cmds.{ResData, Status}

abstract class Response(status: Status.Value, name: String, data: JsValue) {
	val json: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
		"name" -> JsString(name),
		"data" -> data,
	))
}

object Response {
	case class Success[T](resData: ResData[T]) extends Response(Status.Success, resData.name, resData.json)
	case class Failure(msg: String) extends Response(Status.Failure, "line", JsString(msg))
	case class Nothing() extends Response(Status.Nothing, "nothing", JsNull)
}
