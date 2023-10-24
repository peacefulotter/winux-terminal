package terminal.cmds

import models.Response
import terminal.colors.Ansi.{BOLD, RESET}

class HistoryCmd(implicit params: Command.Params) extends Command {
	
	// TODO: restore the ith element on `history i`
	def handle(): Response = {
		val labels = terminal.history.get.zipWithIndex
			.map { case (cmd, i) => f"  $BOLD$i%3s$RESET  $cmd" }
			.toList
		Response.List(labels)
	}
}


