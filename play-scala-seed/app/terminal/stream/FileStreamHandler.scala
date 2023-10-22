package terminal.stream

import models.Response

class FileStreamHandler(params: List[String]) extends LineStreamHandler {
	
	def processLine(line: String, i: Int, session: Int): Option[String] = {
		None
	}
	
	override def done(): Response = {
		// CLOSE FILE
		super.done()
	}
}
