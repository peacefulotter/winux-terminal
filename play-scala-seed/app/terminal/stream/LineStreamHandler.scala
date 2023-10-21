package terminal.stream

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.{DataLine, Response}
import terminal.colors.Ansi

class LineStreamHandler(manager: ActorRef) {
	
	var includeIndices: Boolean = true
	
	def processLine(line: String, i: Int, session: Int): Unit = {
		val prefix = if (includeIndices) s"${Ansi.BRIGHT_BLACK}[$i]${Ansi.RESET}\t" else ""
		val res = Response.Success(DataLine(s"$prefix$line"))
		manager ! SendResponse(session, res)
	}
	
	def done(): Response = Response.Nothing()
}
