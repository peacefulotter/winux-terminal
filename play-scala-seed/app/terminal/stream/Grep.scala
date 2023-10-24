package terminal.stream

import models.Response
import terminal.colors.Ansi

import scala.util.matching.Regex

class Grep(params: List[String]) extends StreamHandler {
	private val regex: Regex = (if (params.isEmpty) "" else params.head).r
	
	private def processLine(line: String): Option[String] =
		if (regex.findFirstIn(line).nonEmpty) {
			val matches = regex.replaceAllIn(line, m => s"${Ansi.RED}${Ansi.BOLD}${m}${Ansi.RESET}")
			Some(matches)
		}
		else
			None
			
	private def processMap[T, U <: Response](data: Map[String, T], creator: Map[String, T] => U): Option[U] = {
		val newMap = data.keySet.map(k => (k, processLine(k))).collect {
			case (k, matched) if matched.nonEmpty => (matched.get, data(k))
		}.toMap
		if (newMap.nonEmpty) Some(creator(newMap)) else None
	}
	
	override def process(res: Response, filter: Boolean = true): Option[Response] =
		if (!filter) Some(res)
		else res match {
			case Response.Nothing() => None
			case Response.Line(text, color, status, replace) => processLine(text) match {
				case Some(text) => Some(Response.Line(text, color, status, replace))
				case _ => None
			}
			case Response.Object(name, data, status, replace) => processMap(data, m => Response.Object(name, m, status, replace))
	        case Response.Flex(data, status, replace) =>
				if (data.isEmpty) return None
				data.head match {
					case _: String =>
						val seq = data.asInstanceOf[Seq[String]].map(processLine).collect {
							case s if s.nonEmpty => s.get
						}
						if (seq.nonEmpty) Some(Response.Flex(seq, status, replace)) else None
					case _ =>
						throw new Error("Impossible match in Grep.process, passed a DataFlex(seq: Seq[T != String]), must be of type DataFlex(seq: Seq[String])")
				}
			case Response.List(data, status, replace) =>
				if (data.isEmpty) return None
				val seq = data
					.map { case (text, color) => (processLine(text), color) }
					.collect { case (text, color) if text.nonEmpty => (text.get, color) }
				if (seq.nonEmpty) Some(Response.List(seq, status, replace)) else None
			case Response.Table(data, status, replace) =>
				processMap(data, (m: Map[String, Any]) => Response.Table(m, status, replace))
			case Response.Path(_) =>
				throw new Error("Impossible match in Grep.process, passed a Response of type DataPath")
			case Response.History(_) =>
				throw new Error("Impossible match in Grep.process, passed a Response of type DataHistory")
			case Response.Autocompletion(_, _) =>
				throw new Error("Impossible match in Grep.process, passed a Response of type DataAutocompletion")
			/*Some(DataAutocompletion(autocompletion, (propositions
				map { case (f, isDir) => (processLine(f), isDir) }
				filter { case (f, _) => f.nonEmpty }
				map { case (f, isDir) => (f.get, isDir) }
			)))*/
		}
}
