package terminal.cmds

import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsArray, JsNull, JsObject, JsString, JsValue, Writes}

abstract class ResData[T](val name: String, val json: JsValue)
	
final case class DataLine[T](data: String)
	extends ResData[T]("line", JsString(data))
	
final case class DataList[T](data: Seq[String])
	extends ResData[T]("list", ResData.arrayToJson(data))
	
final case class DataAutocompletion[T](autocompletion: String, propositions: IndexedSeq[(String, Boolean)] = null)
	extends ResData[T]("autocomplete", ResData.autocompletionToJson(autocompletion, propositions))
	
final case class DataFlex[T, U](data: Seq[U])(implicit writes: Writes[U])
	extends ResData[T]("flex", ResData.arrayToJson(data))
	
final case class DataPath[T](data: String)
	extends ResData[T]("path", JsString(data))
	
final case class DataHistory[T](data: String)
	extends ResData[T]("history", JsString(data))
	

object ResData {
	def arrayToJson[U](arr: Seq[U])(implicit writes: Writes[U]): JsValue = {
		if (arr == null) JsNull else JsArray(arr.map(toJson(_)))
	}
	
	def autocompletionToJson(autocompletion: String, propositions: IndexedSeq[(String, Boolean)]): JsObject = JsObject(Map(
		"autocompletion" -> JsString(autocompletion),
		"propositions" -> ResData.arrayToJson(propositions)
	))
}
