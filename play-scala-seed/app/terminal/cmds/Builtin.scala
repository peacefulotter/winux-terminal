package terminal.cmds

import com.pty4j.{PtyProcess, PtyProcessBuilder}
import models.Response
import os.Path

import scala.language.postfixOps
import java.io.IOException
import scala.io.Source

class Builtin(cmd: String, path: Path) extends Command {
	
	def handle(params: List[String]): Response = {
		try {
			val env = new java.util.HashMap[String, String](System.getenv())
			env.put("TERM", "xterm")
			val command = (List("cd", path.toString(), "&&", cmd) ::: params).toArray
			println(command.mkString(", "))
			val process: PtyProcess = new PtyProcessBuilder()
				.setCommand(command)
				.setEnvironment(env)
				.start
			val is = process.getInputStream
			Source.fromInputStream(is).foreach(print)
			val stdout = Source.fromInputStream(is).mkString
			process.getOutputStream // to avoid closing unused process's stdin in destroy method
			process.waitFor()
			Response.Success(DataLine(stdout))
		}
		catch {
			case e: IOException => Response.Failure(e.getMessage)
			case e: UnsupportedOperationException => Response.Failure(e.getMessage)
			case e: os.SubprocessException => Response.Failure(e.getMessage)
		}
	}
}
