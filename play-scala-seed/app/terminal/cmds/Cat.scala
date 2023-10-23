package terminal.cmds

import models.{DataLine, Response}
import os.{Path, RelPath}
import terminal.stream.StreamHandler

class Cat(implicit params: Command.Params) extends Command {
	
	private def streamRead(file: String): os.Generator[String] =
		os.read.lines.stream( path / RelPath(file) )
	
	protected def pipeStream(file: String): Unit = {
		streamRead(file).zipWithIndex.foreach {
			case (line, i) => streamer.to(DataLine(line))
		}
	}
	
	def handle(): Response = arguments match {
		case file :: _ =>
			// s"${Ansi.BRIGHT_BLACK}[$i]${Ansi.RESET}\t"
			pipeStream(file)
			Response.Nothing()
		case _ => new Response.Failure("cat requires a filename")
	}
}