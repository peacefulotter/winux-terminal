package terminal.cmds

import os.{Path, StatInfo}
import terminal.features.History

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

class HistoryCmd(history: History) extends Command[List[String]] {
	def handle(params: List[String]): Response[List[String]] = {
		val labels = history.get.zipWithIndex
			.map { case (cmd, i) => f"  $i%3s  $cmd" }
			.toList
		Response.Success(labels)(ResType.List)
	}
}
