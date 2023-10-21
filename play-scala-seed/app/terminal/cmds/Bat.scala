package terminal.cmds

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response
import os.{Path, RelPath}
import terminal.Terminal
import terminal.stream.LineStreamHandler

import scala.annotation.tailrec

class Cat(terminal: Terminal, manager: ActorRef, path: Path, session: Int) extends Command {
	
	private case class Params(
		                         file: Option[String] = None,
		                         out: LineStreamHandler = terminal.defaultStreamHandler
	                         )
	
	@tailrec
	private def parseParams(params: List[String], parsed: Params = Params()): Params = params match {
		case x :: xs =>
			if (parsed.file.isEmpty)
				parseParams(xs, Params(Some(x), parsed.out))
			else if (x == "|") {
				val out = terminal.getStreamHandler(xs)
				Params(parsed.file, out)
			}
			// Add other params here if needed
			else
				parsed
		case _ => parsed;
	}
	
	private def sendStartStream(file: String): Unit = {
		val res = Response.Success(DataObject("bat", Map(
			"lang" -> "typescript",
			"file" -> file,
		)))
		manager ! SendResponse(session, res)
	}
	
	private def streamRead(file: String): os.Generator[String] =
		os.read.lines.stream( path / RelPath(file) )
	
	def handle(params: List[String]): Response = parseParams(params) match {
		case Params(Some(file), out) =>
			sendStartStream(file)
			streamRead(file).zipWithIndex.foreach {
				case (line, i) => out.processLine(line, i, session)
			}
			out.done()
		case _ => new Response.Failure("cat requires a filename")
	}
}
