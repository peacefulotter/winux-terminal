package terminal.cmds

import models.Response
import terminal.features.History

class HistoryCmd(history: History) extends Command {
	def handle(params: List[String]): Response = {
		val labels = history.get.zipWithIndex
			.map { case (cmd, i) => f"  $i%3s  $cmd" }
			.toList
		Response.Success(DataList(labels))
	}
}
