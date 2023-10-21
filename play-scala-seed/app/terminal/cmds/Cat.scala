package terminal.cmds

import models.Response
import os.{Path, RelPath}
import terminal.stream.LineStreamHandler

import scala.annotation.tailrec

class Cat(implicit params: Command.Params) extends Command {
	
	protected case class Params(
		                         file: Option[String] = None,
		                         out: LineStreamHandler = new LineStreamHandler(manager)
	                         )
	
	@tailrec
	protected final def parseParams(params: List[String], parsed: Params = Params()): Params = params match {
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
	
	private def streamRead(file: String): os.Generator[String] =
		os.read.lines.stream( path / RelPath(file) )
	
	protected def pipeStream(file: String, out: LineStreamHandler): Response = {
		streamRead(file).zipWithIndex.foreach {
			case (line, i) => out.processLine(line, i, session)
		}
		out.done()
	}
	
	def handle(params: List[String]): Response = parseParams(params) match {
		case Params(Some(file), out) => pipeStream(file, out)
		case _ => new Response.Failure("cat requires a filename")
	}
}