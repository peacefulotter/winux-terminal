package terminal.cmds

import models.Response
import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Ls(path: Path) extends Command {
	
	private def getDirectory(params: List[String]): Either[Path, String] = {
		if (params.nonEmpty)
			PathHelper.getSubPath(path, params.head) match {
				case Failure(exception) => Right(exception.getMessage)
				case Success(dir) => Left(dir)
			}
		else
			Left(path)
	}
	
	def handle(params: List[String]): Response = getDirectory(params) match {
		case Left(dir) =>
			val content = os.walk.attrs(dir, maxDepth = 1)
				.sortBy { case (p, attrs) => attrs.isFile }
				.map { case (p, attrs) =>
					println(p, attrs)
					(PathHelper.getFileName((p, attrs)), attrs.isDir) }
			Response.Success(DataFlex(content))
		case Right(msg) => Response.Failure(msg)
	}
}
