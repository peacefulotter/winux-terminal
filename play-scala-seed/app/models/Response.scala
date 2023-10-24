package models

import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue}
import terminal.cmds.{Colors, Status}

abstract class Response(val name: String, data: Any, status: Status.Value = Status.Success, replace: Boolean = false) {
	val json: JsObject = JsObject(Seq(
		"status" -> JsNumber(status.id),
		"name" -> JsString(name),
		"data" -> Response.toJson(data),
		"replace" -> JsBoolean(replace)
	))
}

object Response {
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
		case s: String => JsString(s)
		case Tuple2(v1, v2) => JsArray(Seq(toJson(v1), toJson(v2)))
		case Tuple3(v1, v2, v3) => JsArray(Seq(toJson(v1), toJson(v2), toJson(v3)))
		case arr: Seq[_] => JsArray(arr map { toJson })
		case map: Map[String, Any] => JsObject(map map {
			case (k, v) => (k, toJson(v))
		})
		case Colors.Text.C(name) => toJson(name)
		case _ => throw JsonConversionException(s"Unsupported element type, got ${elt.getClass.getName}")
	}
	
	private def textColor(color: Colors.Text.C, status: Status.Value): Colors.Text.C = {
		if (status == Status.Error)
			Colors.Text.Error
		else if (color != null)
			color
		else
			null
	}
	
	private def textColor(status: Status.Value): Colors.Text.C = {
		if (status == Status.Error)
			Colors.Text.Error
		else
			Colors.Text.Foreground
	}
	
	
	final case class Line(data: String, color: Colors.Text.C, status: Status.Value, replace: Boolean = false)
		extends Response(
			"line",
			Map(
				"text" -> data,
				"color" -> textColor(color, status)
			),
			status,
			replace
		)
	
	object Line {
		def apply(data: String, color: Colors.Text.C, replace: Boolean = false): Line = Line(data, color, Status.Success, replace)
		def apply(data: String, status: Status.Value = Status.Success, replace: Boolean = false): Line = Line(data, null, status, replace)
		def error(data: String): Line = Line(data, null, Status.Error)
	}
	
	final case class List(data: Seq[(String, Colors.Text.C)], status: Status.Value = Status.Success, replace: Boolean = false)
		extends Response("list", Map("data" -> data), status, replace)
	
	object List {
		def apply(data: Seq[String], colors: Map[Int, Colors.Text.C], status: Status.Value = Status.Success, replace: Boolean = false): List = List(
			data.zipWithIndex.map {
				case (line, i) => (line, colors.getOrElse(i, Colors.Text.Foreground))
			},
			status,
			replace
		)
		
		def apply(data: Seq[String], color: Colors.Text.C, status: Status.Value = Status.Success, replace: Boolean = false): List = List(
			data map { (_, color) }, status, replace
		)
		
		def apply(data: Seq[String], status: Status.Value = Status.Success, replace: Boolean = false): List = List(
			data, textColor(status), status, replace
		)
		
		def error(msg: Seq[String]): List = List(msg, Status.Error)
	}
	
	final case class Autocompletion(autocompletion: String, propositions: IndexedSeq[(String, Boolean)] = null)
		extends Response(
			"autocomplete",
			Map(
				"autocompletion" -> autocompletion,
				"propositions" -> propositions
			)
		)
	
	final case class Flex[T](data: Seq[T], status: Status.Value = Status.Success, replace: Boolean = false)
		extends Response("flex", Map("data" -> data), status, replace)
	
	final case class Table[U](data: Map[String, U], status: Status.Value = Status.Success, replace: Boolean = false)
		extends Response("table", Map("data" -> data), status, replace)
	
	final case class Path(data: String)
		extends Response("path", data)
	
	final case class History(data: String)
		extends Response("history", data)
	
	final case class Object(override val name: String, map: Map[String, Any], status: Status.Value = Status.Success, replace: Boolean = false)
		extends Response(name, map, status, replace)
	
	final case class Nothing()
		extends Response("nothing", null, Status.Nothing)
}

