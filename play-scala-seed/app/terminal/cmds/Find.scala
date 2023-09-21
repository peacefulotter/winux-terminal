package terminal.cmds

import os.{Path, StatInfo}

import scala.util.{Failure, Success, Try}
import scala.annotation.tailrec

class Find(path: Path) extends Command {
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
	
	private def findFile(file: String, depth: Int): Response[String] = {
		// TODO: res whenever we want outside return
		// winux.add( s"Searching for '$file' with depth=$depth..." )

		def skip = (p: Path, stats: StatInfo) =>
			stats.isFile && !p.baseName.startsWith(file)
		
		// TODO: same here -> stream the result
		val content = os.walk.stream.attrs(path, skip, maxDepth=depth)
			.filter { case (p, s) => p.baseName.startsWith(file) }
			
		Response.Nothing()
	}
	
	def handle(params: List[String]): Response[String] = {
		try {
			solveParams(params) match {
				case Solver(Some(file), Some(depth)) => findFile(file, depth)
				case Solver(Some(file), None) => findFile(file, 1)
				case Solver(None, _)  =>
					throw new NullPointerException("need to specify a file or directory to search for")
			}
		}
		catch {
			case e: Exception => Response.Failure(e.getMessage)
		}
	}
}
