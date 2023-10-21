package terminal.cmds

import models.Response

class Err(implicit params: Command.Params) extends Command {
	def handle(params: List[String]): Response = {
		new Response.Failure(f"Error: ${keyword} is not a recognized command")
	}
}
