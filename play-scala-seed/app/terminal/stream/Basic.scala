package terminal.stream

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response
import terminal.cmds.DataLine
import terminal.colors.Ansi

class Basic(manager: ActorRef) extends LineStreamHandler {
	
	override def processLine(line: String, i: Int): Unit = {
		val res = Response.Success(DataLine(s"${Ansi.BRIGHT_BLACK}[$i]${Ansi.RESET}\t$line"))
		manager ! SendResponse(res)
	}
	
	override def done(): Response = Response.Nothing()
}
