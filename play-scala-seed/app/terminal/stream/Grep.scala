package terminal.stream

import models.{DataAutocompletion, DataFlex, DataHistory, DataLine, DataList, DataNothing, DataObject, DataPath, DataTable, ResponseData}
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
			
	private def processMap[T, U <: ResponseData[_]](data: Map[String, T], creator: Map[String, T] => U): Option[U] = {
		val newMap = data.keySet.map(k => (k, processLine(k))).collect {
			case (k, matched) if matched.nonEmpty => (matched.get, data(k))
		}.toMap
		if (newMap.nonEmpty) Some(creator(newMap)) else None
	}
	
	override def process(data: ResponseData[_], filter: Boolean = true): Option[ResponseData[_]] =
		if (!filter) Some(data)
		else data match {
			case DataNothing() => None
			case DataLine(data, color) => processLine(data) match {
				case Some(line) => Some(DataLine(line, color))
				case _ => None
			}
			case DataObject(name, data) => processMap(data, m => DataObject(name, m))
	        case DataFlex(data) =>
				if (data.isEmpty) return None
				data.head match {
					case _: String =>
						val seq = data.asInstanceOf[Seq[String]].map(processLine).collect {
							case s if s.nonEmpty => s.get
						}
						if (seq.nonEmpty) Some(DataFlex(seq)) else None
					case _ =>
						throw new Error("Impossible match in Grep.process, passed a DataFlex(seq: Seq[T != String]), must be of type DataFlex(seq: Seq[String])")
				}
			case DataList(data) =>
				if (data.isEmpty) return None
				val seq = data
					.map { case (text, color) => (processLine(text), color) }
					.collect { case (text, color) if text.nonEmpty => (text.get, color) }
				if (seq.nonEmpty) Some(DataList(seq)) else None
			case DataTable(data) =>
				processMap(data, (m: Map[String, Any]) => DataTable(m))
			case DataPath(_) =>
				throw new Error("Impossible match in Grep.process, passed a ResponseData of type DataPath")
			case DataHistory(data) =>
				throw new Error("Impossible match in Grep.process, passed a ResponseData of type DataHistory")
			case DataAutocompletion(_, _) =>
				throw new Error("Impossible match in Grep.process, passed a ResponseData of type DataAutocompletion")
			/*Some(DataAutocompletion(autocompletion, (propositions
				map { case (f, isDir) => (processLine(f), isDir) }
				filter { case (f, _) => f.nonEmpty }
				map { case (f, isDir) => (f.get, isDir) }
			)))*/
		}
}
