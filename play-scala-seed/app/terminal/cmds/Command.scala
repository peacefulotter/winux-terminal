package terminal.cmds

import models.Response

abstract class Command {
	def handle(params: List[String]): Response
}
