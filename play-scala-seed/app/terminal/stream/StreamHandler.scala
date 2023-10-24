package terminal.stream

import models.Response

trait StreamHandler {
	def process(res: Response, filter: Boolean = true): Option[Response]
	def done(): Response = Response.Nothing()
}
