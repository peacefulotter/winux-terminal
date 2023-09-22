package terminal.cmds

import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsArray, JsString, JsValue, Writes}

abstract class ResData[T](val name: String, val json: JsValue)
final case class DataEmpty[T]() extends ResData[T]("empty", null)
final case class DataLine[T](data: String) extends ResData[T]("line", JsString(data))
final case class DataList[T](data: List[String]) extends ResData[T]("list", JsArray(data.map(JsString)))
final case class DataFlex[T, U](data: List[U])(implicit writes: Writes[U]) extends ResData[T]("flex", JsArray(data.map(toJson(_))))
final case class DataPath[T](data: String) extends ResData[T]("path", JsString(data))
final case class DataInput[T](data: String) extends ResData[T]("input", JsString(data))
