package terminal.cmds
import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue}

abstract class ResData[T](val name: String, data: T) {
	val json: JsValue = ResData.toJson(data)
}
	
final case class DataLine(data: String)
	extends ResData("line", data)
	
final case class DataList(data: Seq[String])
	extends ResData("list", data)
	
final case class DataAutocompletion(autocompletion: String, propositions: IndexedSeq[(String, Boolean)] = null)
	extends ResData("autocomplete", Map(
		"autocompletion" -> autocompletion,
		"propositions" -> propositions
	))
	
final case class DataFlex[T](data: Seq[T])
	extends ResData("flex", data)

final case class DataTable[T <: Map[String, String]](data: Seq[T])
	extends ResData("table", data)
	
final case class DataPath(data: String)
	extends ResData("path", data)
	
final case class DataHistory(data: String)
	extends ResData("history", data)
	

object ResData {
	private case class JsonConversionException(private val message: String = "", private val cause: Throwable = None.orNull)
		extends Exception(message, cause)
	
	def toJson[U](elt: U): JsValue = elt match {
		case n if n == null => JsNull
		case js: JsValue => js
		case b: Boolean => JsBoolean(b)
		case n: Int => JsNumber(n)
		case s: String => JsString (s)
		case Tuple2(v1, v2) => JsArray(Seq(toJson(v1), toJson(v2)))
		case Tuple3(v1, v2, v3) => JsArray(Seq(toJson(v1), toJson(v2), toJson(v3)))
		case arr: Seq[_] => JsArray(arr map { toJson })
		case map: Map[String, _] => JsObject(map map {
			case (k, v) => (k, toJson(v))
		})
		case _ => throw JsonConversionException(s"Unsupported element type, got ${elt.getClass.getName}")
	}
}
