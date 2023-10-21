package terminal.cmds

import akka.actor.ActorRef
import models.Response
import os.Path
import terminal.Terminal

abstract class Command(implicit params: Command.Params)
{
	val terminal: Terminal = params.terminal
	val manager: ActorRef = params.manager
	val path: Path = params.path
	val session: Int = params.session
	val keyword: String = params.keyword
	def handle(params: List[String]): Response
}

object Command {
	case class Params(
		                 terminal: Terminal,
		                 manager: ActorRef,
		                 path: Path,
		                 session: Int,
		                 keyword: String
	                 )
}
