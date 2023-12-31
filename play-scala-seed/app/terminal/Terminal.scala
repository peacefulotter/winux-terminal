package terminal

import akka.actor.{ActorRef, ActorSystem}
import models.Response
import os.Path
import terminal.cmds._
import terminal.features.{Autocomplete, History}
import terminal.helpers.InputHelper.parseInput

import scala.concurrent.ExecutionContext

class Terminal(manager: ActorRef)(implicit system: ActorSystem, implicit val ec: ExecutionContext) {
	// features
	val history = new History
	val autocomplete = new Autocomplete

	def handleCommand(text: String, path: Path, session: Int): Response = {
		val input = parseInput(text)
		if (input.isEmpty) {
			return Response.Nothing()
		}
		
		val (keyword, arguments) = (input.head, input.tail)
		implicit val params: Command.Params = Command.Params(this, manager, path, session, keyword, arguments)
		lazy val commands = Map(
			"bat" -> new Bat,
			"cat" -> new Cat,
			"cd" -> new Cd,
			"ls" -> new Ls,
			"find" -> new Find,
			"history" -> new HistoryCmd,
			"colors" -> new Colors,
			"system" -> new System,
			"top" -> new Top
		)
		// TODO: help command
		val cmd = commands.getOrElse(keyword, new Builtin)
		
		val res = cmd.handle()
		println(s"RES from command before streamer: $res")
		val processed = cmd.streamer.respond(res)
		println(s"RES from command after streamer: $processed")
		
		history.push(text)
		processed
	}
	
	// TODO: implement
	private def handleKill(): Unit = println("===== killing")
}
