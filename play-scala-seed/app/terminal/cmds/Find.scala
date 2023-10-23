package terminal.cmds

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.{DataLine, Response}
import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success, Try}
import scala.annotation.tailrec

class Find(implicit params: Command.Params) extends Command {
	private case class Solver(file: Option[String], depth: Option[Int])
	
	@tailrec
	private def solveArguments(args: List[String], solver: Solver = Solver(None, None)): Solver = {
		args match {
			case arg :: xs => arg match {
				case s"--depth=$d" => Try(d.toInt) match {
					case Success(depth) => solveArguments(xs, Solver(solver.file, Some(depth)))
					case Failure(_) => throw new NumberFormatException(s"depth parameter should be int, got $d")
				}
				case file => solveArguments(xs, Solver(Some(file), solver.depth))
			}
			case _ => solver
		}
	}
	
	private def findFile(file: String, depth: Int): Response = {
		streamer.to(DataLine(
			s"Searching for '$file' with depth=$depth...",
			Colors.Text.Info
		), filter=false)
		
		def skip: (Path, StatInfo) => Boolean = (p: Path, stats: StatInfo) => {
			stats.isFile && !PathHelper.withExtension(p).matches(file)
		}
		
		os.walk.stream.attrs(path, skip, maxDepth=depth)
			.filter { case (p, s) => s.isFile }
			.foreach { c =>
				streamer.to(DataLine(
					PathHelper.getFileName(c, fullPath = true)
				))
			}
			
		Response.Nothing()
	}
	
	def handle(): Response = {
		try {
			solveArguments(arguments) match {
				case Solver(Some(file), Some(depth)) => findFile(file, depth)
				case Solver(Some(file), None) => findFile(file, 1)
				case Solver(None, _)  => new Response.Failure("Need to specify a file or directory to search for")
			}
		}
		catch {
			case e: Exception => new Response.Failure(e.getMessage)
		}
	}
}
