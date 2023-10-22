package terminal.cmds


import managers.ActorRefManager.SendResponse
import models.{DataObject, Response}

class Bat(implicit params: Command.Params) extends Cat {
	
	private def sendStartStream(file: String): Unit = {
		val res = Response.Success(DataObject("bat", Map(
			"lang" -> "typescript",
			"file" -> file,
		)))
		manager ! SendResponse(session, res)
	}
	
	override def handle(): Response = arguments match {
		case file :: _ =>
			// TODO: out.includeIndices = false
			sendStartStream(file)
			val out = null // TODO: out in command.params
			pipeStream(file, out)
		case _ => new Response.Failure("bat requires a filename")
	}
}
