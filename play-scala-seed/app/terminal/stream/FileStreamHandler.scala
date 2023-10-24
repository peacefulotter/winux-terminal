package terminal.stream

import models.Response

class FileStreamHandler(params: List[String]) extends StreamHandler {
	
	 private val filename: String = {
		if (params.isEmpty)
			throw new Error("FileStreamHandler (>) requires to specify a filename")
		else
			params.head
	}
	
	override def process(res: Response, filter: Boolean = true): Option[Response] = {
		println(res.json, filename)
		???
		None
	}
	
	override def done(): Response = {
		// CLOSE FILE
		super.done()
	}
}
