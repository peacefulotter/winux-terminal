package terminal.cmds

import models.Response

class Bat(implicit params: Command.Params) extends Cat {
	
	private def sendStartStream(file: String): Unit = {
		val data = Response.Object("bat", Map(
			"lang" -> "typescript",
			"file" -> file,
		))
		streamer.send(data, filter=false)
	}
	
	override def handle(): Response = arguments match {
		case file :: _ =>
			sendStartStream(file)
			pipeStream(file)
			Response.Nothing()
		case _ => Response.Line.error("bat requires a filename")
	}
}
