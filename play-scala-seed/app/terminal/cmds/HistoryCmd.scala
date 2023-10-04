package terminal.cmds

import models.Response
import terminal.colors.Ansi.{BOLD, RESET}
import terminal.features.History

class HistoryCmd(history: History) extends Command {
	
	// TODO: restore the ith element on `history i`
	def handle(params: List[String]): Response = {
		val labels = history.get.zipWithIndex
			.map { case (cmd, i) => f"  $BOLD$i%3s$RESET  $cmd" }
			.toList
		Response.Success(DataList(labels))
	}
}
