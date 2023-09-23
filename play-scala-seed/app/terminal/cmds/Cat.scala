package terminal.cmds

import models.Response

class Cat() extends Command {
	def handle(params: List[String]): Response = {
		Response.Success(DataLine("cat"))
	}
}
