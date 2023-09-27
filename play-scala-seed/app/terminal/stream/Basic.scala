package terminal.stream

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response
import terminal.cmds.DataLine

class Basic(manager: ActorRef) extends LineStreamHandler {
	
	override def processLine(line: String): Unit = {
		val res = Response.Success(DataLine(line))
		manager ! SendResponse(res)
	}
	
	override def done(): Response = Response.Nothing()
}
