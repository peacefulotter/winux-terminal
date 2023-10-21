package models

import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue}
import terminal.cmds.Colors

abstract class ResponseData[T](val name: String, data: T) {
	val json: JsValue = ResponseData.toJson(data)
}

final case class DataLine(data: String, color: Colors.Text.C = null)
	extends ResponseData("line", Map(
		"text" -> data,
		"color" -> (if (color != null) color.name else null)
	))

final case class DataList(data: Seq[(String, String)])
	extends ResponseData("list", Map("data" -> data))

object DataList {
	def default(data: Seq[String]): DataList = DataList(
		data map { s => (s, Colors.Text.Foreground.name) }
	)
	def withColors(data: Seq[(String, Colors.Text.C)]): DataList = DataList(
		data map { case (text, color) => (text, color.name) }
	)
}

final case class DataAutocompletion(autocompletion: String, propositions: IndexedSeq[(String, Boolean)] = null)
	extends ResponseData("autocomplete", Map(
		"autocompletion" -> autocompletion,
		"propositions" -> propositions
	))

final case class DataFlex[T](data: Seq[T])
	extends ResponseData("flex", Map("data" -> data))

final case class DataTable[U](data: Map[String, U])
	extends ResponseData("table", Map("data" -> data))

final case class DataPath(data: String)
	extends ResponseData("path", data)

final case class DataHistory(data: String)
	extends ResponseData("history", data)

final case class DataObject(override val name: String, map: Map[String, Any])
	extends ResponseData(name, map)

final case class DataNothing()
	extends ResponseData("nothing", null)


object ResponseData {
	private case class JsonConversionException(private val message: String = "", private val cause: Throwable = None.orNull)
		extends Exception(s"Exception when building a Json Response: $message", cause)
	
	private def toJson[U](elt: U): JsValue = elt match {
		case n if n == null => JsNull
		case js: JsValue => js
		case b: Boolean => JsBoolean(b)
		case i: Int => JsNumber(i)
		case l: Long => JsNumber(l)
		case d: Double => JsNumber(d)
		case bd: BigDecimal => JsNumber(bd)
		case s: String => JsString (s)
		case Tuple2(v1, v2) => JsArray(Seq(toJson(v1), toJson(v2)))
		case Tuple3(v1, v2, v3) => JsArray(Seq(toJson(v1), toJson(v2), toJson(v3)))
		case arr: Seq[_] => JsArray(arr map { toJson })
		case map: Map[String, Any] => JsObject(map map {
			case (k, v) => (k, toJson(v))
		})
		case _ => throw JsonConversionException(s"Unsupported element type, got ${elt.getClass.getName}")
	}
}

