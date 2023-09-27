package terminal.cmds

import models.Response
import os.Path
import terminal.Terminal
import terminal.stream.LineStreamHandler

class Cat(terminal: Terminal, path: Path) extends Command {
	
	private case class Params(
		                         file: Option[String] = None,
		                         out: LineStreamHandler = terminal.defaultStreamHandler
	                         )
	
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
	
	private def streamRead(file: String): os.Generator[String] =
		os.read.lines.stream( path / file )
	
	def handle(params: List[String]): Response = parseParams(params) match {
		case Params(Some(file), out) =>
			streamRead(file).foreach(out.processLine)
			out.done()
		case _ => Response.Failure("cat requires a filename")
	}
}
