package terminal.features

import os.{Path, StatInfo}
import terminal.cmds.{DataInput, Response}
import terminal.helpers.{InputHelper, PathHelper}

import scala.util.{Failure, Success}

class Autocomplete {
	// returns ( <partial+optional>childFolder, matched folders and files)
	private type Child = Option[String]
	private type Candidates = IndexedSeq[(Path, StatInfo)]
	
	private def getCompletionCandidates(cmd: String, path: Path): Either[(Child, Candidates), String] = {
		if (cmd.isEmpty || cmd.last == ' ') {
			val candidates = os.walk.attrs(path, maxDepth = 1)
			return Left(None, candidates)
		}
		
		// returns (parentFolder, <partial+optional>childFolder)
		def getFolders(input: String): (String, Option[String]) = {
			if (input.last == '/')
				(input, Option.empty[String])
			else {
				val folders = input.split("/")
				(folders.dropRight(1).mkString("/"), Some(folders.last))
			}
		}
		
		InputHelper.parseInput(cmd) match {
			case Nil => Right("")
			case input =>
				val (parent, child) = getFolders(input.last)
				PathHelper.getSubPath(path, parent) match {
					case Failure(exception) =>
						Right(exception.getMessage)
					case Success(parentPath) =>
						def skip: (Path, StatInfo) => Boolean = child match {
							case Some(partialFolder) => (p: Path, _: StatInfo) => !p.baseName.startsWith(partialFolder)
							case None => (_: Path, _: StatInfo) => false
						}
						val candidates = os.walk.attrs(parentPath, skip = skip, maxDepth = 1)
						Left((child, candidates))
				}
		}
	}
	
	private def autocomplete(cmd: String, path: Path, child: Child, candidates: Candidates): Response[String] = {
		def getTextDropChildFolder: String =
			child match {
				case Some(partialFolder) => cmd.dropRight(partialFolder.length)
				case None => cmd
			}
		
		if (candidates.length == 1) {
			val autocompleted = getTextDropChildFolder + PathHelper.getFileName(candidates.head)
			Response.Success(DataInput(autocompleted))
		}
		
		else if (candidates.length > 1) {
			val candidatePaths = candidates.map(_._1.baseName)
			val commonPath = candidatePaths.fold(candidatePaths.head)((acc, cur) => acc.intersect(cur))
			val autocompleted = getTextDropChildFolder + commonPath
			// TODO:
			// winux.add(Components.filesList(candidates))
			Response.Success(DataInput(autocompleted))
		}
		
		else
			Response.Nothing()
	}
	
	def handle(cmd: String, path: Path): Response[String] = {
		getCompletionCandidates(cmd, path) match {
			case Left((child, candidates)) => autocomplete(cmd, path, child, candidates)
			case Right(msg) => Response.Failure(msg)
			case _ => Response.Nothing();
		}
	}
}
