package terminal.stream

import models.Response

trait LineStreamHandler {
	def processLine(line: String): Unit
	
	// If the handler accumulates some data as it processes lines
	// Then it can flush that data as a Response back to the client
	def done(): Response
}
