package terminal.cmds

import akka.actor.ActorRef
import models.Response
import os.Path
import terminal.Terminal
import terminal.stream.Streamer

abstract class Command(implicit params: Command.Params)
{
	val terminal: Terminal = params.terminal
	val path: Path = params.path
	val session: Int = params.session
	val keyword: String = params.keyword
	val arguments: List[String] = params.arguments
	val streamer: Streamer = new Streamer(arguments, params.manager, session)
	
	def handle(): Response
}

object Command {
	case class Params(
		                 terminal: Terminal,
		                 manager: ActorRef,
		                 path: Path,
		                 session: Int,
		                 keyword: String,
		                 arguments: List[String]
	                 )
}
