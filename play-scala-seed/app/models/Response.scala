package models

import play.api.libs.json.{JsBoolean, JsNumber, JsObject, JsString, JsValue, OWrites}
import terminal.cmds.{Colors, Status}

abstract class Response(status: Status.Value, name: String, data: JsValue, replace: Boolean = false) {
	val json: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
		"name" -> JsString(name),
		"data" -> data,
		"replace" -> JsBoolean(replace)
	))
}

// intermediate class between Response class and Response case classes
// To be able to pass a generic typed resData while preserving Response to be non generic
abstract class ResponseProxy[T](status: Status.Value, data: ResponseData[T], replace: Boolean = false)
	extends Response(status, data.name, data.json, replace)

object Response {
	case class Success[T](data: ResponseData[T], replace: Boolean = false)
		extends ResponseProxy(Status.Success, data, replace)
	
	case class Failure(msg: Seq[(String, Colors.Text.C)])
		extends ResponseProxy(Status.Failure, DataList.withColors(msg)) {
		def this(msg: String) = this(List((msg, Colors.Text.Error)))
	}
		
	case class Nothing()
		extends ResponseProxy(Status.Nothing, DataNothing())
}
