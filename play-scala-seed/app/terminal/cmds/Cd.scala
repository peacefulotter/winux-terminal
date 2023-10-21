package terminal.cmds

import models.{DataPath, Response}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Cd(implicit params: Command.Params) extends Command {
	def handle(params: List[String]): Response = {
		if ( params.isEmpty )
			return Response.Nothing()
		
		PathHelper.getSubPath(path, params.head) match {
			case Success(path) =>
				if (os.isDir(path))
					Response.Success(DataPath(path.toString()))
				else
					new Response.Failure(f"$path is not a directory")
			case Failure(exception) =>
				new Response.Failure(exception.getMessage)
		}
	}
}

