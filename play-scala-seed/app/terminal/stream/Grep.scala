package terminal.stream

import akka.actor.ActorRef
import models.Response

import scala.util.matching.Regex

class Grep(manager: ActorRef, params: List[String]) extends Basic(manager) {
	private val regex: Regex = (if (params.isEmpty) "" else params.head).r
	// Can process other params if needed
	
	override def processLine(line: String): Unit = {
		val matches = regex.replaceAllIn(line, m => s"\u001b[1m\u001b[31m${m}\u001b[0m" )
		if (regex.findFirstIn(line).nonEmpty) {
			println(s"line: $line, regex: $regex, matches: $matches")
			super.processLine(matches)
		}
	}
	
	override def done(): Response = super.done()
}
