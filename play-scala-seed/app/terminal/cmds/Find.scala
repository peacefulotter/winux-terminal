package terminal.cmds

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response
import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success, Try}
import scala.annotation.tailrec

class Find(manager: ActorRef, path: Path) extends Command {
	private case class Solver(file: Option[String], depth: Option[Int])
	
	@tailrec
	private def solveParams(params: List[String], solver: Solver = Solver(None, None)): Solver = {
		params match {
			case param :: xs => param match {
				case s"--depth=$d" => Try(d.toInt) match {
					case Success(depth) => solveParams(xs, Solver(solver.file, Some(depth)))
					case Failure(_) => throw new NumberFormatException(s"depth parameter should be int, got $d")
				}
				case file => solveParams(xs, Solver(Some(file), solver.depth))
			}
			case _ => solver
		}
	}
	
	private def findFile(file: String, depth: Int): Response = {
		manager ! SendResponse(Response.Success(DataLine(
			s"Searching for '$file' with depth=$depth..."
		)))
		
		def skip: (Path, StatInfo) => Boolean = (p: Path, stats: StatInfo) => {
			stats.isFile && !PathHelper.withExtension(p).matches(file)
		}
		
		os.walk.stream.attrs(path, skip, maxDepth=depth)
			.filter { case (p, s) => s.isFile }
			.foreach { c =>
				manager ! SendResponse(Response.Success(DataLine(
					PathHelper.getFileName(c, fullPath = true)
				)))
			}
			
		Response.Success(DataLine("done."))
	}
	
	def handle(params: List[String]): Response = {
		try {
			solveParams(params) match {
				case Solver(Some(file), Some(depth)) => findFile(file, depth)
				case Solver(Some(file), None) => findFile(file, 1)
				case Solver(None, _)  => Response.Failure("Need to specify a file or directory to search for")
			}
		}
		catch {
			case e: Exception => Response.Failure(e.getMessage)
		}
	}
}
