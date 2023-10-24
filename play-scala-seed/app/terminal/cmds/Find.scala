package terminal.cmds

import models.Response
import os.{Path, StatInfo}
import terminal.helpers.PathHelper

import scala.util.{Failure, Success, Try}
import scala.annotation.tailrec

class Find(implicit params: Command.Params) extends Command {
	private case class Parsed(file: Option[String], depth: Option[Int])
	
	@tailrec
	private def parseArguments(args: List[String], parsed: Parsed = Parsed(None, None)): Parsed = {
		args match {
			case arg :: xs => arg match {
				case s"--depth=$d" => Try(d.toInt) match {
					case Success(depth) => parseArguments(xs, Parsed(parsed.file, Some(depth)))
					case Failure(_) => throw new NumberFormatException(s"depth parameter should be int, got $d")
				}
				case file => parseArguments(xs, Parsed(Some(file), parsed.depth))
			}
			case _ => parsed
		}
	}
	
	private def findFile(file: String, depth: Int): Response = {
		streamer.send(Response.Line(
			s"Searching for '$file' with depth=$depth...",
			Colors.Text.Info
		), filter=false)
		
		def skip: (Path, StatInfo) => Boolean = (p: Path, stats: StatInfo) => {
			stats.isFile && !PathHelper.withExtension(p).matches(file)
		}
		
		os.walk.stream.attrs(path, skip, maxDepth=depth)
			.filter { case (_, s) => s.isFile }
			.foreach { c =>
				streamer.send(Response.Line(
					PathHelper.getFileName(c, fullPath = true)
				))
			}
		
		Response.Nothing()
	}
	
	def handle(): Response = {
		try {
			parseArguments(arguments) match {
				case Parsed(Some(file), Some(depth)) => findFile(file, depth)
				case Parsed(Some(file), None) => findFile(file, 1)
				case Parsed(None, _)  => Response.Line.error("Need to specify a file or directory to search for")
			}
		}
		catch {
			case e: Exception => Response.Line.error(e.getMessage)
		}
	}
}
