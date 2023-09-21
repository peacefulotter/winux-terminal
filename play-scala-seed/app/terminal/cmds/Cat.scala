package terminal.cmds

class Cat() extends Command[String] {
	def handle(params: List[String]): Response[String] = {
		Response.Success("cat")(ResType.Line)
	}
}
