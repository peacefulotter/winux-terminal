package models

import play.api.libs.json.{JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue, OWrites}
import terminal.cmds.{ResData, Status}

abstract class Response(status: Status.Value, name: String, data: JsValue, replace: Boolean = false) {
	val json: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
		"name" -> JsString(name),
		"data" -> data,
		"replace" -> JsBoolean(replace)
	))
}

object Response {
	case class Success[T](resData: ResData[T], replace: Boolean = false)
		extends Response(Status.Success, resData.name, resData.json, replace)
		
	case class Failure(msg: String)
		extends Response(Status.Failure, "line", JsString(msg))
		
	case class Nothing()
		extends Response(Status.Nothing, "nothing", JsNull)
}
