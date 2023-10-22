package terminal.stream

import models.Response

trait LineStreamHandler {
	def processLine(line: String, i: Int, session: Int): Option[String]
	def done(): Response = Response.Nothing()
}
