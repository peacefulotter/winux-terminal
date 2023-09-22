package terminal.cmds

import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}

class Ls(path: Path) extends Command[List[(String, Boolean)]] {
	
	private def getDirectory(params: List[String]): Either[Path, String] = {
		if (params.nonEmpty)
			PathHelper.getSubPath(path, params.head) match {
				case Failure(exception) => Right(exception.getMessage)
				case Success(dir) => Left(dir)
			}
		else
			Left(path)
	}
	
	def handle(params: List[String]): Response[List[(String, Boolean)]] = getDirectory(params) match {
		case Left(dir) =>
			val content = os.walk.attrs(dir, maxDepth = 1)
				.sortBy { case (p, attrs) => attrs.isFile }
				.map { case (p, attrs) => (p.baseName, attrs.isDir) }
				.toList
			Response.Success(DataFlex(content))
		case Right(msg) => Response.Failure(msg)
	}
}
