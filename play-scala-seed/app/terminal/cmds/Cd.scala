package terminal.cmds

import terminal.Terminal
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Cd(terminal: Terminal) extends Command[String] {
	def handle(params: List[String]): Response[String] = {
		if ( params.isEmpty )
			return Response.Nothing[String]()
		
		PathHelper.getSubPath(terminal.getPath, params.head) match {
			case Success(path) =>
				if (os.isDir(path))
					Response.Success(DataPath(path.toString()))
				else
					Response.Failure(f"$path is not a directory")
			case Failure(exception) =>
				Response.Failure(exception.getMessage)
		}
	}
}
