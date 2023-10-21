package terminal.features

import models.{DataAutocompletion, Response}
import os.{Path, StatInfo}
import terminal.helpers.{InputHelper, PathHelper}

import scala.util.{Failure, Success}

class Autocomplete() {
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
	
	private def autocomplete(cmd: String, path: Path, child: Child, candidates: Candidates): Response = {
		def getTextDropChildFolder: String =
			child match {
				case Some(partialFolder) => cmd.dropRight(partialFolder.length)
				case None => cmd
			}
		
		def getCommonPrefix(a: String, b: String): String =
			a.zip(b).takeWhile(Function.tupled(_ == _)).map(_._1).mkString
			
		if (candidates.length == 1) {
			val autocompletion = getTextDropChildFolder + PathHelper.getFileName(candidates.head)
			Response.Success(DataAutocompletion(autocompletion))
		}
		
		else if (candidates.length > 1) {
			val propositions = candidates
				.map { c => (PathHelper.getFileName(c), c._2.isDir) }
				.sortBy { case (name, isDir) => (isDir, name) }
			val commonPath = propositions.foldLeft(propositions.head._1)((acc, cur) => getCommonPrefix(acc, cur._1))
			val autocompletion = getTextDropChildFolder + commonPath
			Response.Success(DataAutocompletion(autocompletion, propositions))
		}
		
		else
			Response.Nothing()
	}
	
	def handle(cmd: String, path: Path): Response = {
		getCompletionCandidates(cmd, path) match {
			case Left((child, candidates)) => autocomplete(cmd, path, child, candidates)
			case Right(msg) => new Response.Failure(msg)
			case _ => Response.Nothing();
		}
	}
}