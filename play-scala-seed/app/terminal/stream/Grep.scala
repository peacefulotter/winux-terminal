package terminal.stream

import akka.actor.ActorRef
import models.Response
import terminal.colors.Ansi

import scala.util.matching.Regex

class Grep(manager: ActorRef, params: List[String]) extends Basic(manager) {
	private val regex: Regex = (if (params.isEmpty) "" else params.head).r
	// Can process other params if needed
	
	override def processLine(line: String, i: Int): Unit = {
		val matches = regex.replaceAllIn(line, m => s"${Ansi.RED}${Ansi.BOLD}${m}${Ansi.RESET}" )
		if (regex.findFirstIn(line).nonEmpty) {
			super.processLine(matches, i)
		}
	}
	
	override def done(): Response = super.done()
}
