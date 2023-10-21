package terminal

import akka.actor.{ActorRef, ActorSystem}
import models.Response
import os.Path
import terminal.cmds._
import terminal.features.{Autocomplete, History}
import terminal.helpers.InputHelper.parseInput
import terminal.stream.{Grep, LineStreamHandler}

import scala.concurrent.ExecutionContext

class Terminal(manager: ActorRef)(implicit system: ActorSystem, implicit val ec: ExecutionContext) {
	// features
	val history = new History
	val autocomplete = new Autocomplete

	private def getCommand(text: String, path: Path, session: Int): (Option[Command], List[String]) = {
		val input = parseInput(text)
		if (input.isEmpty) {
			return (Option.empty[Command], Nil)
		}
		
		val (keyword, params) = (input.head, input.tail)
		implicit val cmdParams: Command.Params = Command.Params(this, manager, path, session, keyword)
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
		(Some(cmd), params)
	}
	
	def handleCommand(text: String, path: Path, session: Int): Response = {
		getCommand(text, path, session) match {
			case (Some(cmd), params) =>
				val res = cmd.handle(params)
				history.push(text)
				println(s"RES from command $res")
				res
			case _ => Response.Nothing()
		}
	}

	def getStreamHandler(params: List[String]): LineStreamHandler = params match {
		case cmd :: xs => cmd match {
			case "grep" => new Grep(manager, xs)
			case _ => new LineStreamHandler(manager)
		}
		case _ => new LineStreamHandler(manager)
	}
	
	// TODO: implement
	private def handleKill(): Unit = println("===== killing")
}
