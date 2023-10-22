package terminal.cmds

import models.Response
import os.{Path, RelPath}
import terminal.stream.LineStreamHandler

class Cat(implicit params: Command.Params) extends Command {
	
	private def streamRead(file: String): os.Generator[String] =
		os.read.lines.stream( path / RelPath(file) )
	
	protected def pipeStream(file: String, out: LineStreamHandler): Response = {
		streamRead(file).zipWithIndex.foreach {
			case (line, i) => out.processLine(line, i, session)
		}
		out.done()
	}
	
	def handle(): Response = arguments match {
		case file :: _ =>
			val out = null // TODO: out in command.params
			pipeStream(file, out)
		case _ => new Response.Failure("cat requires a filename")
	}
}