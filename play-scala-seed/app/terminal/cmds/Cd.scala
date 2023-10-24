package terminal.cmds

import models.Response
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Cd(implicit params: Command.Params) extends Command {
	
	def handle(): Response = arguments match {
		case dir :: _ =>
			PathHelper.getSubPath(path, dir) match {
				case Success(path) =>
					if (os.isDir(path))
						Response.Path(path.toString())
					else
						Response.Line.error(f"$path is not a directory")
				case Failure(exception) =>
					Response.Line.error(exception.getMessage)
			}
		case _ => Response.Nothing()
	}
}

