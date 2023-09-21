package terminal.cmds

class Err(keyword: String) extends Command[String] {
	def handle(params: List[String]): Response[String] = {
		Response.Failure(f"Error: ${keyword} is not a recognized command")
	}
}
