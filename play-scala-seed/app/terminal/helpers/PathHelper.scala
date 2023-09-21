package terminal.helpers

import os.{Path, StatInfo}
import scala.util.{Success, Try}

object PathHelper {
	def getFileName(file: (Path, StatInfo), fullPath: Boolean = false): String = file match {
		case (path, attrs) =>
			(if (fullPath) path.toString else path.baseName) +
				(if (attrs.isDir) "/" else "." + path.ext)
	}
	
	def getSubPath(path: Path, str: String): Try[Path] = 
		if (str.isEmpty)
			Try { path }
		else
			Try {
				str.split("/").foldLeft(path)(
					(acc, cur) => cur match {
						case ".." => acc / os.up
						case "." => acc
						case _ => acc / cur
					}
				)
			}
}
