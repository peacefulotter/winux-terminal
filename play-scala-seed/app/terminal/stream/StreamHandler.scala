package terminal.stream

import models.{Response, ResponseData}

trait StreamHandler {
	def process(data: ResponseData[_], filter: Boolean = true): Option[ResponseData[_]]
	def done(): Response = Response.Nothing()
}
