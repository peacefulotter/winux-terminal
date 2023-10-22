package terminal.cmds

import models.Response

class Err(implicit params: Command.Params) extends Command {
	def handle(): Response = {
		new Response.Failure(f"Error: ${keyword} is not a recognized command")
	}
}
