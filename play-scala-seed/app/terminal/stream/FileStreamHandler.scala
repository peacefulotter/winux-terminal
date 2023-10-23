package terminal.stream

import models.{DataAutocompletion, DataFlex, DataLine, DataList, DataNothing, DataTable, Response, ResponseData}

class FileStreamHandler(params: List[String]) extends StreamHandler {
	
	 private val filename: String = {
		if (params.isEmpty)
			throw new Error("FileStreamHandler (>) requires to specify a filename")
		else
			params.head
	}
	
	override def process(data: ResponseData[_], filter: Boolean = true): Option[ResponseData[_]] = {
		println(data.json, filename)
		???
		None
	}
	
	override def done(): Response = {
		// CLOSE FILE
		super.done()
	}
}
