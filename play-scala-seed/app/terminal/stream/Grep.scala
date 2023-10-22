package terminal.stream

import terminal.colors.Ansi

import scala.util.matching.Regex

class Grep(params: List[String]) extends LineStreamHandler {
	private val regex: Regex = (if (params.isEmpty) "" else params.head).r
	
	override def processLine(line: String, i: Int, session: Int): Option[String] = {
		if (regex.findFirstIn(line).nonEmpty) {
			val matches = regex.replaceAllIn(line, m => s"${Ansi.RED}${Ansi.BOLD}${m}${Ansi.RESET}" )
			Some(matches)
		}
		else
			None
	}
}
