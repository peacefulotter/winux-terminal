package terminal.cmds

import models.Response

class Err(keyword: String) extends Command {
	def handle(params: List[String]): Response = {
		Response.Failure(f"Error: ${keyword} is not a recognized command")
	}
}
