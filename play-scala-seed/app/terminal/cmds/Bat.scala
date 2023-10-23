package terminal.cmds

import models.{DataObject, Response}

class Bat(implicit params: Command.Params) extends Cat {
	
	private def sendStartStream(file: String): Unit = {
		val data = DataObject("bat", Map(
			"lang" -> "typescript",
			"file" -> file,
		))
		streamer.to(data, filter=false)
	}
	
	override def handle(): Response = arguments match {
		case file :: _ =>
			sendStartStream(file)
			pipeStream(file)
			Response.Nothing()
		case _ => new Response.Failure("bat requires a filename")
	}
}
