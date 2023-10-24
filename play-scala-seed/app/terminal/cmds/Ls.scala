package terminal.cmds

import models.Response
import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Ls(implicit params: Command.Params) extends Command {
	
	private def getDirectory: Either[Path, String] = arguments match {
		case dir :: _ =>
			PathHelper.getSubPath(path, dir) match {
				case Failure(exception) => Right(exception.getMessage)
				case Success(dir) => Left(dir)
			}
		case _ => Left(path)
	}
	
	def handle(): Response = getDirectory match {
		case Left(dir) =>
			val content = os.walk.attrs(dir, maxDepth = 1)
				.sortBy { case (p, attrs) => attrs.isFile }
				.map { case (p, attrs) =>
					(PathHelper.getFileName((p, attrs)), attrs.isDir)
				}
			Response.Flex(content)
		case Right(msg) => Response.Line.error(msg)
	}
}


