package terminal.cmds

import terminal.features.History

class HistoryCmd(history: History) extends Command[List[String]] {
	def handle(params: List[String]): Response[List[String]] = {
		val labels = history.get.zipWithIndex
			.map { case (cmd, i) => f"  $i%3s  $cmd" }
			.toList
		Response.Success(DataList(labels))
	}
}
